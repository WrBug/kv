package com.wrbug.kv.compile.runner

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import com.sun.tools.javac.code.Symbol
import com.wrbug.kv.annotation.KV
import com.wrbug.kv.compile.Global
import com.wrbug.kv.compile.matcher.MethodMatcherManager
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier

class KVTaskRunner(element: Element) : TaskRunner(element) {
    companion object {
        const val SUFFIX = "Impl"
        private const val FIELD_PROVIDER = "provider"
        private const val METHOD_INIT = "init"
        private const val METHOD_GET_APPLICATION = "getApplication"
        private const val METHOD_GET_PROVIDER = "getDataProvider"
        private val CLASS_KV = ClassName.get("com.wrbug.kv", "KV")
        private val CLASS_ENV = ClassName.get("com.wrbug.kv", "Env")
        private val CLASS_DATA_PROVIDER = ClassName.get("com.wrbug.kv", "DataProvider")
    }

    override fun run() {
        val spec = buildImplClass()
        element.enclosedElements.asSequence().filter {
            it is Symbol.MethodSymbol && it.getKind() == ElementKind.METHOD && !it.isStaticOrInstanceInit && it.modifiers.contains(
                Modifier.PUBLIC
            )
        }.map { it as Symbol.MethodSymbol }.forEach {
            MethodMatcherManager.getMatcherManager(it)?.buildMethod(it)?.let {
                spec.addMethod(it.build())
            }
        }
        saveClass(spec, packageName)
    }

    private fun buildConstructorMethod(): MethodSpec.Builder {
        val scope = element.getAnnotation(KV::class.java).scope
        val name =
            element.getAnnotation(KV::class.java).value.ifEmpty { element.simpleName.toString() }
        return MethodSpec.constructorBuilder()
            .addStatement("\$L=\$L.\$L()", FIELD_PROVIDER, CLASS_KV, METHOD_GET_PROVIDER)
            .addStatement(
                "\$L.\$L(\$T.\$L(),\"\$L\",\"\$L\")",
                FIELD_PROVIDER,
                METHOD_INIT,
                CLASS_ENV,
                METHOD_GET_APPLICATION, scope, name
            )
    }

    private fun buildImplClass(): TypeSpec.Builder {

        return TypeSpec.classBuilder(className + SUFFIX)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addSuperinterface(ClassName.get(element.asType()))
            .addField(CLASS_DATA_PROVIDER, FIELD_PROVIDER, Modifier.PRIVATE)
            .addMethod(buildConstructorMethod().build())
    }
}