package com.wrbug.kv.runtime

import com.wrbug.kv.runtime.compileonly.DataProviderManager

object InnerKV {

    fun init(context: Any) {
    }

    fun getDataProvider(): Any? {
        return DataProviderManager.getDataProvider()
    }
}