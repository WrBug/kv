package com.wrbug.kv.compile.matcher

import com.squareup.javapoet.MethodSpec
import com.sun.tools.javac.code.Symbol

object PutMethodMatcher : MethodMatcher {
    override fun match(symbol: Symbol.MethodSymbol): Boolean {
        return false
    }

    override fun buildMethod(symbol: Symbol.MethodSymbol): MethodSpec.Builder {
        return MethodSpec.methodBuilder(symbol.name.toString())
    }
}