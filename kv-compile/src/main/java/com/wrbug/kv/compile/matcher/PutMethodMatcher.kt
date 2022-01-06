package com.wrbug.kv.compile.matcher

import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.TypeTag
import com.wrbug.kv.annotation.KVGet
import com.wrbug.kv.annotation.KVPut
import com.wrbug.kv.compile.codebuilder.CodeBuilderManager
import com.wrbug.kv.compile.util.toFirstLowerCase
import javax.lang.model.element.Modifier

object PutMethodMatcher : MethodMatcher {
    override fun match(symbol: Symbol.MethodSymbol): Boolean {
        if (symbol.getAnnotation(KVPut::class.java) == null
            && !symbol.name.startsWith("set")
            && !symbol.name.startsWith("put")
        ) {
            return false
        }
        if (symbol.returnType.tag != TypeTag.VOID) {
            return false
        }
        return symbol.params().size == 1
    }

    override fun buildMethod(symbol: Symbol.MethodSymbol): MethodSpec.Builder {
        val key = symbol.getAnnotation(KVGet::class.java)?.key ?: symbol.name.substring(
            3,
            symbol.name.length
        ).toFirstLowerCase()
        val builder = MethodSpec.methodBuilder(symbol.name.toString()).addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override::class.java)
            .addParameter(TypeName.get(symbol.params()[0].asType()), key)
        CodeBuilderManager.getCodeBuilder(symbol.params()[0].asType())
            ?.buildPutCode(builder, symbol, key)
        return builder
    }
}