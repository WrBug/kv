package com.wrbug.kv.compile.runner

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import com.wrbug.kv.runtime.DataProviderCreator
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier

class DataProviderCreatorTaskRunner(element: Element) : TaskRunner(element) {
    companion object {
        const val SUFFIX = "DataProviderCreator"
        private const val METHOD_NEW_INSTANCE = "newInstance"
    }

    override fun run() {
        val spec = buildImplClass()
        spec.addMethod(buildNewInstanceMethod().build())
        saveClass(spec, packageName)
    }
    private fun buildNewInstanceMethod(): MethodSpec.Builder {
        return MethodSpec.methodBuilder(METHOD_NEW_INSTANCE)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addAnnotation(Override::class.java)
            .returns(Any::class.java)
            .addStatement(
                "return new \$T()",
                ClassName.get(packageName, className + KVTaskRunner.SUFFIX)
            )
    }

    private fun buildImplClass(): TypeSpec.Builder {
        return TypeSpec.classBuilder(className + SUFFIX)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addSuperinterface(DataProviderCreator::class.java)
    }
}