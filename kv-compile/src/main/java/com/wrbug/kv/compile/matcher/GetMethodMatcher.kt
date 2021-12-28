package com.wrbug.kv.compile.matcher

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Type
import com.sun.tools.javac.code.TypeTag
import com.wrbug.kv.annotation.KVGet
import com.wrbug.kv.compile.runner.KVImplTaskRunner
import com.wrbug.kv.compile.util.toFirstLowerCase
import javax.lang.model.element.Modifier

object GetMethodMatcher : MethodMatcher {
    private const val METHOD_GET_STRING = "getString"
    private const val METHOD_GET_INT = "getInt"
    private const val METHOD_GET_LONG = "getLong"
    private const val METHOD_GET_FLOAT = "getFloat"
    private const val METHOD_GET_DOUBLE = "getDouble"
    private const val METHOD_GET_BOOLEAN = "getBoolean"
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
                ParameterSpec.builder(TypeName.get(param.asType()), "arg_0", Modifier.FINAL)
                    .addModifiers(param.modifiers)
                    .build()
            )
        }
        if (param != null) {
            builder.addStatement("return arg_0")
        } else if (symbol.returnType.isPrimitive) {
            when (symbol.returnType.tag) {
                TypeTag.BYTE, TypeTag.CHAR, TypeTag.SHORT,
                TypeTag.INT -> builder.addStatement(
                    "return (\$T)\$L.\$L(\"\$L\",0)",
                    symbol.returnType,
                    KVImplTaskRunner.FIELD_PROVIDER,
                    METHOD_GET_INT,
                    key
                )
                TypeTag.LONG -> builder.addStatement(
                    "return \$L.\$L(\"\$L\",0)", KVImplTaskRunner.FIELD_PROVIDER,
                    METHOD_GET_LONG, key
                )
                TypeTag.FLOAT -> builder.addStatement(
                    "return \$L.\$L(\"\$L\",0)", KVImplTaskRunner.FIELD_PROVIDER,
                    METHOD_GET_FLOAT, key
                )
                TypeTag.DOUBLE -> builder.addStatement(
                    "return \$L.\$L(\"\$L\",0)", KVImplTaskRunner.FIELD_PROVIDER,
                    METHOD_GET_DOUBLE, key
                )
                TypeTag.BOOLEAN -> builder.addStatement(
                    "return \$L.\$L(\"\$L\",false)", KVImplTaskRunner.FIELD_PROVIDER,
                    METHOD_GET_BOOLEAN, key
                )
            }
        } else {
            builder.addStatement("return null")
        }
        return builder
    }
}