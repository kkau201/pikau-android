package com.kaylakautai.data.testing

import com.kaylakautai.data.util.PikauLogger

class FakePikauLogger : PikauLogger {
    val info = mutableListOf<Pair<String, String>>()

    override fun d(tag: String, message: String) = Unit

    override fun i(tag: String, message: String) {
        info += tag to message
    }

    override fun w(tag: String, message: String, throwable: Throwable?) = Unit

    override fun e(tag: String, message: String, throwable: Throwable?) = Unit
}
