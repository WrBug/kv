package com.wrbug.kv.sample

import android.app.Application
import com.tencent.mmkv.MMKV
import com.wrbug.kv.KV

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KV.init(this)
        MMKV.initialize(this)
    }
}