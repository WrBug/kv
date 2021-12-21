package com.wrbug.kv

import android.app.Application

object KV {
    @JvmStatic
    fun init(application: Application) {
        Env.application = application
    }
}