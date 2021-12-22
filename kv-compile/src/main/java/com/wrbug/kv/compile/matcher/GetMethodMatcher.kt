package com.wrbug.kv.compile.matcher

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Type
import com.wrbug.kv.annotation.KVGet
import com.wrbug.kv.compile.util.toFirstLowerCase
import javax.lang.model.element.Modifier

object GetMethodMatcher : MethodMatcher {
    override fun match(symbol: Symbol.MethodSymbol): Boolean {
        if (!symbol.name.startsWith("get") && symbol.getAnnotation(KVGet::class.java) == null) {
            return false
        }
        if (symbol.returnType is Type.JCVoidType) {
            return false
        }
        if (symbol.params().size > 1) {
            return false
        }
        val type = symbol.params().getOrNull(0)?.asType()
        return type == null || type.toString() == symbol.returnType.toString()
    }

    override fun buildMethod(symbol: Symbol.MethodSymbol): MethodSpec.Builder {
        val key = symbol.getAnnotation(KVGet::class.java)?.key ?: symbol.name.substring(
            3,
            symbol.name.length
        ).toFirstLowerCase()
        val param = symbol.params().getOrNull(0)
        val builder = MethodSpec.methodBuilder(symbol.name.toString())
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .returns(ClassName.get(symbol.returnType))

            .addStatement("\$T key= \"\$L\"", String::class.java, key)
        if (param != null) {
            builder.addParameter(
                ParameterSpec.builder(TypeName.get(param.asType()), "arg_0", Modifier.FINAL)
                    .addModifiers(param.modifiers)
                    .build()
            )
        }
        if (param != null) {
            builder.addStatement("return arg_0")
        } else if (symbol.returnType.isPrimitive) {
            if (symbol.returnType.isNumeric) {
                builder.addStatement("return 0")
            } else {
                builder.addStatement("return false")
            }
        } else {
            builder.addStatement("return null")
        }
        return builder
    }
}