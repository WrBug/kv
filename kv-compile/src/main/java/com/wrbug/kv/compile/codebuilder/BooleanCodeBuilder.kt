package com.wrbug.kv.compile.codebuilder

import com.squareup.javapoet.MethodSpec
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Type
import com.sun.tools.javac.code.TypeTag
import com.wrbug.kv.compile.matcher.GetMethodMatcher
import com.wrbug.kv.compile.runner.KVImplTaskRunner

object BooleanCodeBuilder : CodeBuilder {
    private const val METHOD_GET_BOOLEAN = "getBoolean"
    private const val METHOD_PUT_BOOLEAN = "putBoolean"

    override fun isMatch(type: Type): Boolean {
        return type.isPrimitive && type.tag == TypeTag.BOOLEAN
    }


    override fun buildPutCode(
        builder: MethodSpec.Builder,
        symbol: Symbol.MethodSymbol,
        key: String
    ) {
        builder.addStatement(
            "\$1L.\$2L(\"\$3L\",\$3L)", KVImplTaskRunner.FIELD_PROVIDER,
            METHOD_PUT_BOOLEAN, key
        )
    }


    override fun buildGetCode(
        builder: MethodSpec.Builder,
        symbol: Symbol.MethodSymbol,
        key: String
    ) {
        if (symbol.params().isNullOrEmpty()) {
            builder.addStatement(
                "\$T \$L=false", Boolean::class.java,
                GetMethodMatcher.PARAMETER_DEFAULT_VALUE
            )
        }
        builder.addStatement(
            "return \$L.\$L(\"\$L\",\$L)", KVImplTaskRunner.FIELD_PROVIDER,
            METHOD_GET_BOOLEAN, key, GetMethodMatcher.PARAMETER_DEFAULT_VALUE
        )
    }


}