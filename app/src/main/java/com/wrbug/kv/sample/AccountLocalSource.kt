package com.wrbug.kv.sample

import com.wrbug.kv.annotation.KV
import com.wrbug.kv.annotation.KVGet

@KV
interface AccountLocalSource {
    var id: Long
    var name: String
    fun isMale(): Boolean
    fun setMale(male: Boolean)
    var user: User
    fun getMap(): Map<String, String>

    fun removeMap()

    fun clear()

    fun commit()

    fun apply()
}

class User {
    var name: String? = ""
    var age: Int? = 0
}