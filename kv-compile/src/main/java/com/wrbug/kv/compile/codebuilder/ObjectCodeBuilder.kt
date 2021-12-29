package com.wrbug.kv.compile.codebuilder

import com.google.gson.reflect.TypeToken
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Type
import com.wrbug.kv.compile.matcher.GetMethodMatcher
import com.wrbug.kv.compile.runner.KVImplTaskRunner
import com.wrbug.kv.compile.util.CLASS_ENV
import com.wrbug.kv.compile.util.KV_PACKAGE

object ObjectCodeBuilder : CodeBuilder {
    private const val METHOD_GET_STRING = "getString"
    val CLASS_OBJECT_CONVERTER = ClassName.get(KV_PACKAGE, "ObjectConverter")
    override fun isMatch(type: Type) = true

    override fun buildGetCode(
        builder: MethodSpec.Builder,
        symbol: Symbol.MethodSymbol,
        key: String
    ) {
        if (symbol.params().isNullOrEmpty()) {
            builder.addStatement(
                "\$T \$L=null", symbol.returnType,
                GetMethodMatcher.PARAMETER_DEFAULT_VALUE
            )
        }
        builder.addStatement(
            "\$T result = \$L.\$L(\"\$L\",null)",
            String::class.java,
            KVImplTaskRunner.FIELD_PROVIDER,
            METHOD_GET_STRING,
            key
        )
        builder.addStatement(
            "\$T converter=\$L.getObjectConverter()", CLASS_OBJECT_CONVERTER,
            CLASS_ENV
        )
            .addStatement(
                "\$T type = new \$T<\$T>() {}.getType()",
                java.lang.reflect.Type::class.java,
                TypeToken::class.java,
                symbol.returnType
            )
            .addStatement("\$1T obj=(\$1T)converter.convert(result,type)",symbol.returnType)
            .addStatement("return obj==null?\$L:obj", GetMethodMatcher.PARAMETER_DEFAULT_VALUE)

    }
}