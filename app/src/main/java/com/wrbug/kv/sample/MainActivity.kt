package com.wrbug.kv.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.wrbug.kv.KV
import com.wrbug.kv.util.toJson

class MainActivity : AppCompatActivity() {
    private lateinit var valueEt: EditText
    private lateinit var textTv: TextView
    private var source: AccountLocalSource? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        source = KV.get(AccountLocalSource::class.java)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.saveBtn)
        valueEt = findViewById<EditText>(R.id.valueEt)
        textTv = findViewById<TextView>(R.id.textTv)
        update()
        btn.setOnClickListener {
            source?.name = valueEt.text.toString()
            update()
        }
    }

    private fun update() {
        textTv.text = source?.name
    }
}