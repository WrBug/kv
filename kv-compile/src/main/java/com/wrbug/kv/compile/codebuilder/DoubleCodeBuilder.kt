package com.wrbug.kv.compile.codebuilder

import com.squareup.javapoet.MethodSpec
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Type
import com.sun.tools.javac.code.TypeTag
import com.wrbug.kv.compile.matcher.GetMethodMatcher
import com.wrbug.kv.compile.runner.KVImplTaskRunner

object DoubleCodeBuilder : CodeBuilder {
    private const val METHOD_GET_DOUBLE = "getDouble"
    private const val METHOD_PUT_DOUBLE = "putDouble"

    override fun isMatch(type: Type): Boolean {
        return type.isPrimitive && type.tag == TypeTag.DOUBLE
    }

    override fun buildPutCode(
        builder: MethodSpec.Builder,
        symbol: Symbol.MethodSymbol,
        key: String
    ) {
        builder.addStatement(
            "\$1L.\$2L(\"\$3L\",\$3L)", KVImplTaskRunner.FIELD_PROVIDER,
            METHOD_PUT_DOUBLE, key
        )
    }

    override fun buildGetCode(
        builder: MethodSpec.Builder,
        symbol: Symbol.MethodSymbol,
        key: String
    ) {
        if (symbol.params().isNullOrEmpty()) {
            builder.addStatement(
                "\$T \$L=0", Double::class.java,
                GetMethodMatcher.PARAMETER_DEFAULT_VALUE
            )
        }
        builder.addStatement(
            "return \$L.\$L(\"\$L\",\$L)", KVImplTaskRunner.FIELD_PROVIDER,
            METHOD_GET_DOUBLE, key, GetMethodMatcher.PARAMETER_DEFAULT_VALUE
        )
    }


}