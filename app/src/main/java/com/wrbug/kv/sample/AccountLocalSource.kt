package com.wrbug.kv.sample

import com.wrbug.kv.annotation.KV

@KV
interface AccountLocalSource {
    val id: Long
    val count: Int
    val test: Short
    val amount: Double
    fun isVip(default: Boolean): Boolean
    fun isMale(): Boolean
}