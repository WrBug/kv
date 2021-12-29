package com.wrbug.kv.compile.util

import com.squareup.javapoet.ClassName

const val RUNTIME_ONLY_PACKAGE = "com.wrbug.kv.runtime.compileonly"

const val KV_PACKAGE = "com.wrbug.kv"

val CLASS_ENV = ClassName.get(KV_PACKAGE, "Env")

