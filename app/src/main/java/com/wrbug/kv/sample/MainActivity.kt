package com.wrbug.kv.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.wrbug.kv.KV

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val source = KV.get(AccountLocalSource::class.java)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.saveBtn)
        val valueEt = findViewById<EditText>(R.id.valueEt)
        val textTv = findViewById<TextView>(R.id.textTv)
        textTv.text = source?.isMale().toString()
        btn.setOnClickListener {

        }
    }
}