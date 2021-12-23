package com.wrbug.kv.compile.runner

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import com.wrbug.kv.compile.Global
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier


abstract class TaskRunner(
    protected val element: Element
) {
    val className: String = element.simpleName.toString()
    val packageName = element.toString().let { it.substring(0, it.lastIndexOf(".")) }


    abstract fun run()
    fun safeRun() {
        run()
    }


    fun loge(msg: Any?) {
        Global.loge(msg)
    }

    protected fun saveClass(spec: TypeSpec.Builder, packageName: String) {
        val javaFile =
            JavaFile.builder(packageName, spec.build())
                .build()
        javaFile.writeTo(Global.filer)
    }
}