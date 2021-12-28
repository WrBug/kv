package com.wrbug.kv.compile.codebuilder

import com.squareup.javapoet.MethodSpec
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Type
import com.sun.tools.javac.code.TypeTag
import com.wrbug.kv.compile.matcher.GetMethodMatcher
import com.wrbug.kv.compile.matcher.GetMethodMatcher.PARAMETER_DEFAULT_VALUE
import com.wrbug.kv.compile.runner.KVImplTaskRunner

object LongCodeBuilder : CodeBuilder {
    private const val METHOD_GET_LONG = "getLong"
    override fun isMatch(type: Type): Boolean {
        return type.isPrimitive && type.tag == TypeTag.LONG
    }

    override fun buildPutCode() {
    }

    override fun buildGetCode(
        builder: MethodSpec.Builder,
        symbol: Symbol.MethodSymbol,
        key: String
    ) {
        if (symbol.params().isNullOrEmpty()) {
            builder.addStatement("\$T \$L=0L", Long::class.java, PARAMETER_DEFAULT_VALUE)
        }
        builder.addStatement(
            "return \$L.\$L(\"\$L\",\$L)", KVImplTaskRunner.FIELD_PROVIDER,
            METHOD_GET_LONG, key, PARAMETER_DEFAULT_VALUE
        )
    }


}