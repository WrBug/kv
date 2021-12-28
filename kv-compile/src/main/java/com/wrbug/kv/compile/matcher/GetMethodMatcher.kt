package com.wrbug.kv.compile.matcher

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Type
import com.sun.tools.javac.code.TypeTag
import com.wrbug.kv.annotation.KVGet
import com.wrbug.kv.compile.codebuilder.CodeBuilderManager
import com.wrbug.kv.compile.runner.KVImplTaskRunner
import com.wrbug.kv.compile.util.toFirstLowerCase
import javax.lang.model.element.Modifier

object GetMethodMatcher : MethodMatcher {
    const val PARAMETER_DEFAULT_VALUE = "defaultValue"
    private const val METHOD_GET_STRING = "getString"
    private const val METHOD_GET = "get"
    override fun match(symbol: Symbol.MethodSymbol): Boolean {
        if (symbol.getAnnotation(KVGet::class.java) == null) {
            if (!symbol.name.startsWith("get") &&
                !(symbol.returnType.tag == TypeTag.BOOLEAN && symbol.name.startsWith("is"))
            ) {
                return false
            }
        }
        if (symbol.returnType.tag == TypeTag.VOID) {
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
            if (symbol.name.startsWith("is")) 2 else 3, symbol.name.length
        ).toFirstLowerCase()
        val param = symbol.params().getOrNull(0)
        val builder = MethodSpec.methodBuilder(symbol.name.toString())
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .returns(ClassName.get(symbol.returnType))
        if (param != null) {
            builder.addParameter(
                ParameterSpec.builder(
                    TypeName.get(param.asType()),
                    PARAMETER_DEFAULT_VALUE,
                    Modifier.FINAL
                )
                    .addModifiers(param.modifiers)
                    .build()
            )
        }
        CodeBuilderManager.getCodeBuilder(symbol.returnType)
            ?.buildGetCode(builder, symbol, key)
        return builder
    }
}