package com.wrbug.kv.sample

import android.content.Context
import android.content.SharedPreferences
import com.wrbug.kv.DataProvider
import com.wrbug.kv.annotation.KVDataProvider
import com.wrbug.kv.util.fromJson
import com.wrbug.kv.util.toJson
import java.lang.reflect.Type


/**
 *
 *  class: SharedPreferenceDataProvider.kt
 *  author: wrbug
 *  date: 2021/12/22
 *  description：
 *
 */
@KVDataProvider
class TestSharedPreferenceDataProvider : DataProvider {
    private var sharedPreferences: SharedPreferences? = null
    override fun init(context: Context, name: String, scope: String) {
        sharedPreferences = context.getSharedPreferences(scope + "_" + name, Context.MODE_PRIVATE)
    }

    override fun putString(key: String?, value: String?) {
        sharedPreferences.edit { putString(key, value) }
    }

    override fun getString(key: String?, defaultValue: String?): String? {
        return sharedPreferences?.getString(key, defaultValue)
    }

    override fun putInt(key: String?, value: Int) {
        sharedPreferences.edit { putInt(key, value) }
    }

    override fun getInt(key: String?, value: Int): Int {
        return sharedPreferences?.getInt(key, value) ?: value
    }

    override fun putLong(key: String?, value: Long) {
        sharedPreferences.edit { putLong(key, value) }
    }

    override fun getLong(key: String?, value: Long): Long {
        return sharedPreferences?.getLong(key, value) ?: value
    }

    override fun putFloat(key: String?, value: Float) {
        sharedPreferences.edit { putFloat(key, value) }
    }

    override fun getFloat(key: String?, value: Float): Float {
        return sharedPreferences?.getFloat(key, value) ?: value
    }

    override fun putDouble(key: String?, value: Double) {
        sharedPreferences.edit { putString(key, value.toString()) }
    }

    override fun getDouble(key: String?, value: Double): Double {
        return sharedPreferences?.getString(key, value.toString())?.toDouble() ?: value
    }

    override fun putBoolean(key: String?, value: Boolean) {
        sharedPreferences.edit { putBoolean(key, value) }
    }

    override fun getBoolean(key: String?, value: Boolean): Boolean {
        return sharedPreferences?.getBoolean(key, value) ?: value
    }

    override fun remove(key: String?) {
        sharedPreferences.edit { remove(key) }
    }

    override fun clear() {
        sharedPreferences.edit { clear() }
    }

    override fun commit() {
        sharedPreferences.edit { commit() }
    }

    override fun apply() {
        sharedPreferences.edit { }
    }

    private fun SharedPreferences?.edit(action: SharedPreferences.Editor.() -> Unit) {
        this?.edit()?.let {
            it.action()
            it.apply()
        }
    }
}