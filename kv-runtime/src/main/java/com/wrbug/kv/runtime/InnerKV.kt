package com.wrbug.kv.runtime

import com.wrbug.kv.runtime.compileonly.DataProviderManager
import com.wrbug.kv.runtime.compileonly.ImplManager

object InnerKV {

    fun init(context: Any) {
    }

    fun getDataProvider(): Any? {
        return DataProviderManager.getDataProvider()
    }

    @JvmStatic
    fun <T> get(clazz: Class<T>): T? {
        return ImplManager.get(clazz) as? T
    }
}