package com.wrbug.kv.compile.runner

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import com.wrbug.kv.compile.Global
import com.wrbug.kv.compile.util.RUNTIME_ONLY_PACKAGE
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier

class DataProviderManagerTaskRunner(val element: Element?) {
    companion object {
        private val MANAGER_CLASS =
            ClassName.get(RUNTIME_ONLY_PACKAGE, "DataProviderManager")
        private const val METHOD_GET_INSTANCE = "getDataProvider"
    }

    private fun buildGetDataProviderMethod(): MethodSpec.Builder {
        return MethodSpec.methodBuilder(METHOD_GET_INSTANCE)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .returns(Any::class.java).apply {
                if (element == null) {
                    addStatement("return null")
                } else {
                    addStatement("return new \$T()", element.asType())
                }
            }
    }

    fun run() {
        val spec = TypeSpec.classBuilder(MANAGER_CLASS.simpleName())
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(buildGetDataProviderMethod().build())
        saveClass(spec, MANAGER_CLASS.packageName())
    }

    private fun saveClass(spec: TypeSpec.Builder, packageName: String) {
        runCatching {
            val javaFile =
                JavaFile.builder(packageName, spec.build())
                    .build()
            javaFile.writeTo(Global.filer)
        }

    }
}