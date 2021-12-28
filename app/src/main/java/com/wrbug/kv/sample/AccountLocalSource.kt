package com.wrbug.kv.sample

import com.wrbug.kv.annotation.KV
import com.wrbug.kv.annotation.KVGet

@KV
interface AccountLocalSource {
    val id: Long
    val count: Int
    val test: Short
    val amount: Double
    fun isVip(default: Boolean): Boolean
    fun isMale(): Boolean

    @KVGet("username")
    fun getUser(): String
}