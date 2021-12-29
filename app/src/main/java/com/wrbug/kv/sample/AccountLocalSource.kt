package com.wrbug.kv.sample

import com.wrbug.kv.annotation.KV
import com.wrbug.kv.annotation.KVGet

@KV
interface AccountLocalSource {
    val id: Long
    fun isMale(): Boolean
    fun getUser(): User
    fun getMap(): Map<String,String>
}

class User{

}