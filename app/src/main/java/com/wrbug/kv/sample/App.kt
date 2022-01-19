package com.wrbug.kv.sample

import android.app.Application
import com.tencent.mmkv.MMKV
import com.wrbug.kv.KV
import com.wrbug.kv.annotation.KVMultiModule

@KVMultiModule
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KV.init(this)
        MMKV.initialize(this)
    }
}