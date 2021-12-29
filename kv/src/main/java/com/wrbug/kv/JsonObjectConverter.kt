package com.wrbug.kv

import com.wrbug.kv.util.fromJson
import com.wrbug.kv.util.toJson
import java.lang.reflect.Type

object JsonObjectConverter : ObjectConverter {
    override fun convert(obj: Any?): String? {
        return obj.toJson()
    }

    override fun convert(str: String?, type: Type): Any? {
        return str.fromJson(type)
    }
}