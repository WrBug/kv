package com.wrbug.kv.compile.matcher

import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.TypeTag
import com.wrbug.kv.annotation.KVClear
import com.wrbug.kv.annotation.KVGet
import com.wrbug.kv.annotation.KVPut
import com.wrbug.kv.annotation.KVRemove
import com.wrbug.kv.compile.codebuilder.CodeBuilderManager
import com.wrbug.kv.compile.runner.KVImplTaskRunner
import com.wrbug.kv.compile.util.toFirstLowerCase
import javax.lang.model.element.Modifier

object CommitMethodMatcher : MethodMatcher {
    private const val METHOD_COMMIT = "commit"
    override fun match(symbol: Symbol.MethodSymbol): Boolean {
        if (symbol.getAnnotation(KVClear::class.java) == null && symbol.name.toString() != METHOD_COMMIT
        ) {
            return false
        }
        if (symbol.returnType.tag != TypeTag.VOID) {
            return false
        }
        return symbol.params().size == 0
    }

    override fun buildMethod(symbol: Symbol.MethodSymbol): MethodSpec.Builder {
        return MethodSpec.methodBuilder(symbol.name.toString()).addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override::class.java)
            .addStatement("\$L.\$L()", KVImplTaskRunner.FIELD_PROVIDER, METHOD_COMMIT)
    }
}