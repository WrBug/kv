package com.wrbug.kv.compile.runner

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
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
        private const val SUFFIX = "Impl"
    }

    override fun run() {
        val spec = buildImplClass()
        val scope = element.getAnnotation(KV::class.java).scope
        element.enclosedElements.asSequence().filter {
            it is Symbol.MethodSymbol && it.getKind() == ElementKind.METHOD && !it.isStaticOrInstanceInit && it.modifiers.contains(
                Modifier.PUBLIC
            )
        }.map { it as Symbol.MethodSymbol }.forEach {
            MethodMatcherManager.getMatcherManager(it)?.buildMethod(it)?.let {
                spec.addMethod(it.build())
            }
        }
        saveClass(spec)
    }

    private fun buildImplClass(): TypeSpec.Builder {
        return TypeSpec.classBuilder(className + SUFFIX)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addSuperinterface(ClassName.get(element.asType()))

    }

    private fun saveClass(spec: TypeSpec.Builder) {
        val javaFile =
            JavaFile.builder(packageName, spec.build())
                .build()
        javaFile.writeTo(Global.filer)
    }
}