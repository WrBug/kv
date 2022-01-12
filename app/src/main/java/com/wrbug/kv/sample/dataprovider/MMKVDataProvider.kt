package com.wrbug.kv.sample.dataprovider

import android.content.Context
import com.tencent.mmkv.MMKV
import com.wrbug.kv.DataProvider
import com.wrbug.kv.annotation.KVDataProvider

/**
 *
 *  class: MMKVDataProvider.kt
 *  author: wrbug
 *  date: 2022/01/12
 *  description：MMKV数据源
 *
 */

@KVDataProvider
class MMKVDataProvider : DataProvider {
    private var kv: MMKV? = null
    override fun init(context: Context, name: String, scope: String) {
        kv = MMKV.mmkvWithID(scope + "_" + name)
    }

    override fun putString(key: String?, value: String?) {
        kv?.encode(key, value)
    }

    override fun getString(key: String?, defaultValue: String?): String? {
        return kv?.decodeString(key, defaultValue)
    }

    override fun putInt(key: String?, value: Int) {
        kv?.encode(key, value)
    }

    override fun getInt(key: String?, defaultValue: Int): Int {
        return kv?.decodeInt(key, defaultValue) ?: defaultValue
    }

    override fun putLong(key: String?, value: Long) {
        kv?.encode(key, value)

    }

    override fun getLong(key: String?, defaultValue: Long): Long {
        return kv?.getLong(key, defaultValue) ?: defaultValue
    }

    override fun putFloat(key: String?, value: Float) {
        kv?.encode(key, value)

    }

    override fun getFloat(key: String?, defaultValue: Float): Float {
        return kv?.getFloat(key, defaultValue) ?: defaultValue
    }

    override fun putDouble(key: String?, value: Double) {
        kv?.encode(key, value)
    }

    override fun getDouble(key: String?, defaultValue: Double): Double {
        return kv?.decodeDouble(key, defaultValue) ?: defaultValue
    }

    override fun putBoolean(key: String?, value: Boolean) {
        kv?.encode(key, value)
    }

    override fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return kv?.decodeBool(key, defaultValue) ?: defaultValue
    }

    override fun remove(key: String?) {
        kv?.removeValueForKey(key)
    }

    override fun clear() {
        kv?.clearAll()
    }

    override fun commit() {
        kv?.sync()
    }

    override fun apply() {
        kv?.async()
    }
}