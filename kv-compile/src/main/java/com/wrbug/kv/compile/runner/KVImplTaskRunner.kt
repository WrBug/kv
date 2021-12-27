package com.wrbug.kv.compile.runner

import com.squareup.javapoet.*
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Type
import com.wrbug.kv.annotation.KV
import com.wrbug.kv.compile.Global
import com.wrbug.kv.compile.matcher.MethodMatcherManager
import com.wrbug.kv.compile.util.KV_PACKAGE
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier

class KVImplTaskRunner(element: Element) : TaskRunner(element) {
    companion object {
        const val SUFFIX = "Impl"
        const val METHOD_GET_INSTANCE = "getInstance"
        private const val CLASS_HOLDER = "Holder"
        private const val FIELD_PROVIDER = "provider"
        private const val FIELD_INSTANCE = "instance"
        private const val METHOD_INIT = "init"
        private const val METHOD_GET_APPLICATION = "getApplication"
        private const val METHOD_GET_PROVIDER = "getDataProvider"
        private val CLASS_KV = ClassName.get(KV_PACKAGE, "KV")
        private val CLASS_ENV = ClassName.get(KV_PACKAGE, "Env")
        private val CLASS_DATA_PROVIDER = ClassName.get(KV_PACKAGE, "DataProvider")
    }

    private val implName = className + SUFFIX
    private val implClass = ClassName.get(packageName, implName)
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
            .addModifiers(Modifier.PRIVATE)
            .addStatement("\$L=\$L.\$L()", FIELD_PROVIDER, CLASS_KV, METHOD_GET_PROVIDER)
            .addStatement(
                "\$L.\$L(\$T.\$L(),\"\$L\",\"\$L\")",
                FIELD_PROVIDER,
                METHOD_INIT,
                CLASS_ENV,
                METHOD_GET_APPLICATION, scope, name
            )
    }

    private fun buildGetInstanceMethod(): MethodSpec.Builder {
        return MethodSpec.methodBuilder(METHOD_GET_INSTANCE).returns(implClass)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addStatement("return \$L.\$L", CLASS_HOLDER, FIELD_INSTANCE)
    }


    private fun buildImplClass(): TypeSpec.Builder {
        return TypeSpec.classBuilder(implName)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addSuperinterface(ClassName.get(element.asType()))
            .addField(CLASS_DATA_PROVIDER, FIELD_PROVIDER, Modifier.PRIVATE)
            .addMethod(buildConstructorMethod().build())
            .addMethod(buildGetInstanceMethod().build())
            .addType(
                TypeSpec.classBuilder(CLASS_HOLDER)
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                    .addField(
                        FieldSpec.builder(
                            ClassName.get(packageName, implName),
                            FIELD_INSTANCE,
                            Modifier.PRIVATE,
                            Modifier.STATIC,
                            Modifier.FINAL
                        ).initializer("new \$L()", implName).build()
                    )
                    .build()
            )
    }
}