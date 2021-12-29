package com.wrbug.kv

import java.lang.reflect.Type

interface ObjectConverter {
    fun convert(obj: Any?): String?
    fun convert(str: String?, type: Type): Any?
}