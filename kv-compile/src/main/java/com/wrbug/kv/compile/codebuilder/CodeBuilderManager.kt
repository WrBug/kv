package com.wrbug.kv.compile.codebuilder

import com.sun.tools.javac.code.Type

object CodeBuilderManager {
    private val list = arrayOf(
        IntCodeBuilder,
        LongCodeBuilder,
        FloatCodeBuilder,
        DoubleCodeBuilder,
        BooleanCodeBuilder
    )

    fun getCodeBuilder(type: Type): CodeBuilder? {
        return list.find { it.isMatch(type) }
    }
}