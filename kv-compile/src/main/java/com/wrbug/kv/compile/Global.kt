package com.wrbug.kv.compile

import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.tools.Diagnostic


object Global {
    lateinit var filer: Filer
    lateinit var messager: Messager
    var rootDir: String = ""

    fun log(msg: Any?) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg.toString())
    }

    fun loge(msg: Any?) {
        messager.printMessage(Diagnostic.Kind.ERROR, msg.toString())
    }
}
