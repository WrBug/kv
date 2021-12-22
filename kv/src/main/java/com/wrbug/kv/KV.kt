package com.wrbug.kv

import android.app.Application
import com.wrbug.kv.runtime.InnerKV

object KV {
    @JvmStatic
    fun init(application: Application) {
        Env.application = application
        InnerKV.init(application)
    }
}