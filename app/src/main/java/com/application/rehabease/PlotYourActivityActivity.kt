package com.application.rehabease

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PlotYourActivityActivity : AppCompatActivity() {
    private lateinit var openMenu: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plot_your_activity)
        openMenu = findViewById(R.id.image_bars)
        openMenu.setOnClickListener {
            val intent = Intent(this@PlotYourActivityActivity, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}