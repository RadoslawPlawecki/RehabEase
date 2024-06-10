package com.application.rehabease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.application.rehabease.both.RegisterActivity

class TitleActivity : AppCompatActivity() {
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)
        button = findViewById(R.id.getting_started)
        button.setOnClickListener {
            val intent = Intent(this@TitleActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}