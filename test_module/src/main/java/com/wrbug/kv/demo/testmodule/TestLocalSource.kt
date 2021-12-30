package com.wrbug.kv.demo.testmodule

import com.wrbug.kv.annotation.KV

@KV
interface TestLocalSource {
    fun getName(): String

    fun setName(name: String)

    var uid: Int
}