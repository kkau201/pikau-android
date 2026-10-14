package com.kaylakautai.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kaylakautai.ui.theme.PikauShapes
import com.kaylakautai.ui.theme.Spacing

private val HeaderMinHeight = 240.dp
private val PanelShape = PikauShapes.large.copy(
    bottomStart = ZeroCornerSize,
    bottomEnd = ZeroCornerSize
)

@Composable
fun PikauScreenScaffold(
    header: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    bottomAction: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = HeaderMinHeight)
                .statusBarsPadding()
                .padding(horizontal = Spacing.xlarge, vertical = Spacing.medium),
            content = header
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(PanelShape)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(start = Spacing.xlarge, top = Spacing.xlarge, end = Spacing.xlarge)
            ) { content() }

            if (bottomAction != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.medium),
                    contentAlignment = Alignment.Center
                ) { bottomAction() }
            }
        }
    }
}