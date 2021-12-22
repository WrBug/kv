package com.wrbug.kv.compile.runner

import com.wrbug.kv.compile.Global
import javax.lang.model.element.Element


abstract class TaskRunner(
    protected val element: Element
) {
    val className:String = element.simpleName.toString()
    val packageName = element.toString().let { it.substring(0, it.lastIndexOf(".")) }


    abstract fun run()
    fun safeRun() {
        run()
    }


    fun loge(msg: Any?) {
        Global.loge(msg)
    }
}