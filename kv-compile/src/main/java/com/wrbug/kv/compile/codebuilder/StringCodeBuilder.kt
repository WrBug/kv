package com.wrbug.kv.compile.codebuilder

import com.squareup.javapoet.MethodSpec
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Type
import com.wrbug.kv.compile.matcher.GetMethodMatcher
import com.wrbug.kv.compile.runner.KVImplTaskRunner

object StringCodeBuilder : CodeBuilder {
    private const val METHOD_GET_STRING = "getString"
    override fun isMatch(type: Type): Boolean {
        return type.toString() == String::class.java.canonicalName
    }

    override fun buildGetCode(
        builder: MethodSpec.Builder,
        symbol: Symbol.MethodSymbol,
        key: String
    ) {
        if (symbol.params().isNullOrEmpty()) {
            builder.addStatement(
                "\$T \$L=\"\"", String::class.java,
                GetMethodMatcher.PARAMETER_DEFAULT_VALUE
            )
        }
        builder.addStatement(
            "return \$L.\$L(\"\$L\",\$L)", KVImplTaskRunner.FIELD_PROVIDER,
            METHOD_GET_STRING, key, GetMethodMatcher.PARAMETER_DEFAULT_VALUE
        )
    }
}