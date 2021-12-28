package com.wrbug.kv.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


private val gson: Gson by lazy {
    Gson()
}

fun <T> String.fromJson(clazz: Class<T>): T? = fromJson(type = clazz)

inline fun <reified T> String?.fromJson(): T? {
    return fromJson(object : TypeToken<T>() {}.type)
}

fun <T> String?.fromJson(type: Type): T? {
    if (this.isNullOrEmpty()) {
        return null
    }
    return runCatching { gson.fromJson<T>(this, type) }.getOrNull()
}

fun Any?.toJson(): String? {
    this ?: return null
    return runCatching { gson.toJson(this) }.getOrNull()
}