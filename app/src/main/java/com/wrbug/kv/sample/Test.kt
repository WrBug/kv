package com.wrbug.kv.sample

import com.wrbug.kv.annotation.KV

@KV
interface Test {
//    fun putName(name: String)
    fun getName(): Boolean

//    fun clear()
}