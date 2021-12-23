package com.wrbug.kv.compile.runner

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier

class DataProviderManagerTaskRunner(element: Element) : TaskRunner(element) {
    companion object {
        private val MANAGER_CLASS =
            ClassName.get("com.wrbug.kv.runtime.compileonly", "DataProviderManager")
        private const val METHOD_GET_INSTANCE = "getDataProvider"
    }

    private fun buildGetDataProviderMethod(): MethodSpec.Builder {
        return MethodSpec.methodBuilder(METHOD_GET_INSTANCE)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .returns(Any::class.java)
            .addStatement("return new \$T()", element.asType())
    }

    override fun run() {
        val spec = TypeSpec.classBuilder(MANAGER_CLASS.simpleName())
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(buildGetDataProviderMethod().build())
        saveClass(spec, MANAGER_CLASS.packageName())
    }
}