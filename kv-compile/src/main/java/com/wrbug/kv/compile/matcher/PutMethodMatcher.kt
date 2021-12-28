package com.wrbug.kv.compile.matcher

import com.squareup.javapoet.MethodSpec
import com.sun.tools.javac.code.Symbol

object PutMethodMatcher : MethodMatcher {
    private const val METHOD_PUT_STRING = "putString"
    private const val METHOD_PUT_INT = "putInt"
    private const val METHOD_PUT_LONG = "putLong"
    private const val METHOD_PUT_FLOAT = "putFloat"
    private const val METHOD_PUT_DOUBLE = "putDOUBLE"
    private const val METHOD_PUT_BOOLEAN = "putBoolean"
    private const val METHOD_PUT = "put"
    override fun match(symbol: Symbol.MethodSymbol): Boolean {
        return false
    }

    override fun buildMethod(symbol: Symbol.MethodSymbol): MethodSpec.Builder {
        return MethodSpec.methodBuilder(symbol.name.toString())
    }
}