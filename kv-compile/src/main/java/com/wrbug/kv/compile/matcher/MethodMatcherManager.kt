package com.wrbug.kv.compile.matcher

import com.sun.tools.javac.code.Symbol

object MethodMatcherManager {
    private val list = listOf(
        GetMethodMatcher,
        PutMethodMatcher,
        RemoveMethodMatcher,
        ClearMethodMatcher,
        ApplyMethodMatcher,
        CommitMethodMatcher
    )

    fun getMatcherManager(symbol: Symbol.MethodSymbol): MethodMatcher? {
        return list.find { it.match(symbol) }
    }
}