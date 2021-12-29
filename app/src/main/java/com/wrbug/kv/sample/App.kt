package com.wrbug.kv.sample

import android.app.Application
import com.wrbug.kv.KV

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        KV.init(this)
    }
}