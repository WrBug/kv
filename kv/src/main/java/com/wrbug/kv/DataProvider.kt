package com.wrbug.kv

import android.content.Context
import java.lang.reflect.Type

/**
 *
 *  class: DataProvider.kt
 *  author: wrbug
 *  date: 2021/12/22
 *  description：
 *
 */
interface DataProvider {

    fun init(context: Context, name: String, scope: String)

    fun putString(key: String?, value: String?)
    fun getString(key: String?, defaultValue: String?): String?

    fun putInt(key: String?, value: Int)
    fun getInt(key: String?, defaultValue: Int): Int

    fun putLong(key: String?, value: Long)
    fun getLong(key: String?, defaultValue: Long): Long

    fun putFloat(key: String?, value: Float)
    fun getFloat(key: String?, defaultValue: Float): Float


    fun putDouble(key: String?, value: Double)
    fun getDouble(key: String?, defaultValue: Double): Double

    fun putBoolean(key: String?, value: Boolean)
    fun getBoolean(key: String?, defaultValue: Boolean): Boolean

    fun remove(key: String?)

    fun clear()

    fun commit()

    fun apply()
}