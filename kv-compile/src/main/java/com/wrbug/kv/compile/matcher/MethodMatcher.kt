package com.wrbug.kv.compile.matcher

import com.squareup.javapoet.MethodSpec
import com.sun.tools.javac.code.Symbol

interface MethodMatcher {
    fun match(symbol: Symbol.MethodSymbol): Boolean

    fun buildMethod(symbol: Symbol.MethodSymbol): MethodSpec.Builder
}