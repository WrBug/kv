package com.wrbug.kv.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wrbug.kv.KV

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KV.init(application)
        setContentView(R.layout.activity_main)
    }
}