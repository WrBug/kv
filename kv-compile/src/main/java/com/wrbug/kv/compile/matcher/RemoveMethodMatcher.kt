package com.wrbug.kv.compile.matcher

import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.TypeTag
import com.wrbug.kv.annotation.KVGet
import com.wrbug.kv.annotation.KVPut
import com.wrbug.kv.annotation.KVRemove
import com.wrbug.kv.compile.codebuilder.CodeBuilderManager
import com.wrbug.kv.compile.runner.KVImplTaskRunner
import com.wrbug.kv.compile.util.toFirstLowerCase
import javax.lang.model.element.Modifier

object RemoveMethodMatcher : MethodMatcher {
    override fun match(symbol: Symbol.MethodSymbol): Boolean {
        if (symbol.getAnnotation(KVRemove::class.java) == null && !symbol.name.startsWith("remove")
        ) {
            return false
        }
        if (symbol.returnType.tag != TypeTag.VOID) {
            return false
        }
        return symbol.params().size == 0
    }

    override fun buildMethod(symbol: Symbol.MethodSymbol): MethodSpec.Builder {
        val key = symbol.getAnnotation(KVRemove::class.java)?.key ?: symbol.name.substring(
            6,
            symbol.name.length
        ).toFirstLowerCase()
        return MethodSpec.methodBuilder(symbol.name.toString()).addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override::class.java)
            .addStatement("\$L.\$L(\"\$L\")", KVImplTaskRunner.FIELD_PROVIDER, "remove", key)
    }
}