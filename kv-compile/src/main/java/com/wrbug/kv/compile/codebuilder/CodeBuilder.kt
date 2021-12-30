package com.wrbug.kv.compile.codebuilder

import com.squareup.javapoet.MethodSpec
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Type

interface CodeBuilder {
    fun isMatch(type: Type): Boolean
    fun buildPutCode(builder: MethodSpec.Builder, symbol: Symbol.MethodSymbol, key: String) {}
    fun buildGetCode(builder: MethodSpec.Builder, symbol: Symbol.MethodSymbol, key: String)
}