package com.wrbug.kv.compile.codebuilder

import com.squareup.javapoet.MethodSpec
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Type
import com.sun.tools.javac.code.TypeTag
import com.wrbug.kv.compile.matcher.GetMethodMatcher
import com.wrbug.kv.compile.runner.KVImplTaskRunner

object IntCodeBuilder : CodeBuilder {
    private const val METHOD_GET_INT = "getInt"
    override fun isMatch(type: Type): Boolean {
        return type.isPrimitive && type.tag in arrayOf(
            TypeTag.BYTE,
            TypeTag.CHAR,
            TypeTag.SHORT,
            TypeTag.INT
        )
    }

    override fun buildPutCode() {
    }

    override fun buildGetCode(
        builder: MethodSpec.Builder,
        symbol: Symbol.MethodSymbol,
        key: String
    ) {
        if (symbol.params().isNullOrEmpty()) {
            builder.addStatement(
                "\$T \$L=0", Int::class.java,
                GetMethodMatcher.PARAMETER_DEFAULT_VALUE
            )
        }
        builder.addStatement(
            "return (\$T)\$L.\$L(\"\$L\",\$L)",
            symbol.returnType,
            KVImplTaskRunner.FIELD_PROVIDER,
            METHOD_GET_INT,
            key,
            GetMethodMatcher.PARAMETER_DEFAULT_VALUE
        )
    }


}