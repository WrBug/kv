package com.wrbug.kv

import android.app.Application
import com.wrbug.kv.dataprovider.SharedPreferenceDataProvider
import com.wrbug.kv.runtime.InnerKV

object KV {
    @JvmStatic
    fun init(application: Application) {
        Env.application = application
        InnerKV.init(application)
    }

    @JvmStatic
    fun <T> get(clazz: Class<T>): T? {
        return InnerKV.get(clazz)
    }

    @JvmStatic
    fun getDataProvider(): DataProvider {
        return InnerKV.getDataProvider() as? DataProvider ?: SharedPreferenceDataProvider()
    }
}