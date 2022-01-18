package com.wrbug.kv.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.wrbug.kv.KV
import com.wrbug.kv.demo.testmodule.TestLocalSource
import com.wrbug.kv.util.toJson

class MainActivity : AppCompatActivity() {
    private lateinit var valueEt: EditText
    private lateinit var textTv: TextView
    private var source: TestLocalSource? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        source = KV.get(TestLocalSource::class.java)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.saveBtn)
        valueEt = findViewById<EditText>(R.id.valueEt)
        textTv = findViewById<TextView>(R.id.textTv)
        update()
        btn.setOnClickListener {
            source?.setName(valueEt.text.toString())
            update()
        }
    }

    private fun update() {
        textTv.text = source?.getName()
    }
}