package com.kaylakautai.buildlogic

import java.io.File
import java.io.Serializable
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory
import org.gradle.api.DefaultTask
import org.gradle.api.Transformer
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.artifacts.result.ResolvedComponentResult
import org.gradle.api.artifacts.result.ResolvedDependencyResult
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

class OpenSourceCoordinatesTransformer : Transformer<List<String>, ResolvedComponentResult>, Serializable {
    override fun transform(root: ResolvedComponentResult): List<String> {
        val modules = linkedSetOf<String>()
        val seen = mutableSetOf<Any>()
        fun visit(component: ResolvedComponentResult) {
            if (!seen.add(component.id)) return
            val id = component.id
            if (id is ModuleComponentIdentifier &&
                !id.group.startsWith(PROJECT_GROUP) &&
                id.version.isNotBlank() &&
                id.version != "unspecified"
            ) {
                modules += "${id.group}:${id.module}:${id.version}"
            }
            component.dependencies.forEach { dependency ->
                if (dependency is ResolvedDependencyResult) visit(dependency.selected)
            }
        }
        visit(root)
        return modules.toList()
    }
}

abstract class GenerateOpenSourceLibrariesTask : DefaultTask() {

    @get:Input
    abstract val coordinates: ListProperty<String>

    @get:Internal
    abstract val gradleUserHome: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun generate() {
        val directory = outputDirectory.get().asFile
        directory.mkdirs()
        val libraries = readLibraries()
        directory.resolve(OUTPUT_FILE).writeText(libraries.toJson())
        logger.lifecycle("Wrote ${libraries.size} open source libraries to ${directory.resolve(OUTPUT_FILE)}")
    }

    private fun readLibraries(): List<OpenSourceLibrary> {
        val client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build()
        val cacheHome = gradleUserHome.asFile.orNull
        val pomCache = mutableMapOf<String, PomInfo?>()
        val resolved = linkedMapOf<String, OpenSourceLibrary>()
        for (coordinate in coordinates.get()) {
            val parts = coordinate.split(":", limit = 3)
            if (parts.size != 3) continue
            val (group, artifact, version) = parts
            val key = "$group:$artifact"
            if (key in resolved) continue
            val pom = loadPom(client, group, artifact, version, cacheHome, pomCache, mutableSetOf())
            resolved[key] = OpenSourceLibrary(
                displayName = pom?.name ?: artifact,
                detail = key,
                version = version,
                license = pom?.licenseName ?: UNKNOWN_LICENSE,
                licenseUrl = pom?.licenseUrl.orEmpty(),
            )
        }
        return resolved.values.sortedWith(compareBy({ it.displayName.lowercase() }, { it.detail }))
    }

    private fun loadPom(
        client: HttpClient,
        group: String,
        artifact: String,
        version: String,
        cacheHome: File?,
        cache: MutableMap<String, PomInfo?>,
        visiting: MutableSet<String>,
    ): PomInfo? {
        if (version.isBlank() || version.contains("\${")) return null
        val key = "$group:$artifact:$version"
        if (!visiting.add(key)) return null
        return try {
            cache.getOrPut(key) {
                val bytes = cachedPom(cacheHome, group, artifact, version) ?: downloadPom(client, group, artifact, version)
                bytes?.let { parsePom(it) }?.inheritingLicense(version, client, cacheHome, cache, visiting)
            }
        } finally {
            visiting.remove(key)
        }
    }

    private fun PomInfo.inheritingLicense(
        version: String,
        client: HttpClient,
        cacheHome: File?,
        cache: MutableMap<String, PomInfo?>,
        visiting: MutableSet<String>,
    ): PomInfo {
        if (licenseName != null || parent == null) return this
        val group = parent.group.ifBlank { properties["project.groupId"] ?: properties["groupId"].orEmpty() }
        val parentVersion = parent.version.interpolate(properties, version)
        if (group.isBlank() || parent.artifact.isBlank()) return this
        val parentPom = loadPom(client, group, parent.artifact, parentVersion, cacheHome, cache, visiting) ?: return this
        return copy(
            name = name ?: parentPom.name,
            licenseName = parentPom.licenseName,
            licenseUrl = parentPom.licenseUrl,
        )
    }

    private fun downloadPom(client: HttpClient, group: String, artifact: String, version: String): ByteArray? {
        val path = "${group.replace('.', '/')}/$artifact/$version/$artifact-$version.pom"
        for (base in REPOSITORIES) {
            val request = HttpRequest.newBuilder(URI.create("$base$path"))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build()
            val response = runCatching {
                client.send(request, HttpResponse.BodyHandlers.ofByteArray())
            }.getOrNull() ?: continue
            if (response.statusCode() == 200) return response.body()
        }
        logger.warn("No POM found for $group:$artifact:$version")
        return null
    }

    companion object {
        const val OUTPUT_FILE = "open_source_libraries.json"
    }
}

private fun cachedPom(cacheHome: File?, group: String, artifact: String, version: String): ByteArray? {
    val versionDir = cacheHome?.resolve("caches/modules-2/files-2.1/$group/$artifact/$version") ?: return null
    if (!versionDir.isDirectory) return null
    return versionDir.walk().maxDepth(2).firstOrNull { it.isFile && it.extension == "pom" }?.readBytes()
}

private data class OpenSourceLibrary(
    val displayName: String,
    val detail: String,
    val version: String,
    val license: String,
    val licenseUrl: String,
)

private data class PomParent(val group: String, val artifact: String, val version: String)

private data class PomInfo(
    val name: String?,
    val licenseName: String?,
    val licenseUrl: String?,
    val parent: PomParent?,
    val properties: Map<String, String>,
)

private fun parsePom(bytes: ByteArray): PomInfo? {
    val document = runCatching {
        DocumentBuilderFactory.newInstance().apply {
            isNamespaceAware = true
            setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)
            setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
        }.newDocumentBuilder().parse(bytes.inputStream()).documentElement
    }.getOrNull() ?: return null

    val properties = document.child("properties")?.childElements().orEmpty().associate { element ->
        element.localName to element.textContent.trim()
    }
    val parent = document.child("parent")?.let { parent ->
        PomParent(
            group = parent.text("groupId").orEmpty(),
            artifact = parent.text("artifactId").orEmpty(),
            version = parent.text("version").orEmpty(),
        )
    }?.takeIf { it.artifact.isNotBlank() }
    val license = document.child("licenses")?.children("license")?.firstOrNull()
    return PomInfo(
        name = document.text("name")?.replace(Regex("\\s+"), " "),
        licenseName = license?.text("name"),
        licenseUrl = license?.text("url"),
        parent = parent,
        properties = properties,
    )
}

private fun String.interpolate(properties: Map<String, String>, childVersion: String): String {
    var current = this
    repeat(INTERPOLATION_LIMIT) {
        val next = PROPERTY_PATTERN.replace(current) { match ->
            when (val key = match.groupValues[1]) {
                "project.version", "version" -> childVersion
                else -> properties[key.removePrefix("project.")] ?: properties[key] ?: match.value
            }
        }
        if (next == current) return current
        current = next
    }
    return current
}

private fun List<OpenSourceLibrary>.toJson(): String = buildString {
    append("[\n")
    this@toJson.forEachIndexed { index, library ->
        append("  {\n")
        append("    \"name\": ${jsonString(library.displayName)},\n")
        append("    \"detail\": ${jsonString(library.detail)},\n")
        append("    \"version\": ${jsonString(library.version)},\n")
        append("    \"license\": ${jsonString(library.license)},\n")
        append("    \"licenseUrl\": ${jsonString(library.licenseUrl)}\n")
        append("  }")
        if (index != this@toJson.lastIndex) append(",")
        append("\n")
    }
    append("]\n")
}

private fun jsonString(value: String): String = buildString {
    append('"')
    value.forEach { char ->
        when (char) {
            '\\' -> append("\\\\")
            '"' -> append("\\\"")
            '\n' -> append("\\n")
            '\r' -> append("\\r")
            '\t' -> append("\\t")
            else -> append(char)
        }
    }
    append('"')
}

private fun Element.child(localName: String): Element? =
    childNodes.elements().firstOrNull { it.localName == localName }

private fun Element.children(localName: String): List<Element> =
    childNodes.elements().filter { it.localName == localName }

private fun Element.childElements(): List<Element> = childNodes.elements()

private fun Element.text(localName: String): String? =
    child(localName)?.textContent?.trim()?.takeIf { it.isNotEmpty() }

private fun NodeList.elements(): List<Element> =
    (0 until length).mapNotNull { item(it) }.filter { it.nodeType == Node.ELEMENT_NODE }.map { it as Element }

private const val PROJECT_GROUP = "com.kaylakautai"
private const val UNKNOWN_LICENSE = "Unknown"
private const val INTERPOLATION_LIMIT = 5
private val PROPERTY_PATTERN = Regex("\\\$\\{([^}]+)}")
private val REPOSITORIES = listOf(
    "https://dl.google.com/dl/android/maven2/",
    "https://repo.maven.apache.org/maven2/",
)
