package com.application.rehabease

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class YourActivityActivity : AppCompatActivity() {
    private lateinit var openMenu: ImageView
    private lateinit var expandShareYourTrainingImage: ImageView
    private lateinit var expandPlotYourActivityImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_activity)
        openMenu = findViewById(R.id.image_bars)
        openMenu.setOnClickListener {
            val intent = Intent(this@YourActivityActivity, MenuActivity::class.java)
            startActivity(intent)
        }
        expandShareYourTrainingImage = findViewById(R.id.angle_down_1)
        expandShareYourTrainingImage.setOnClickListener {
            val intent = Intent(this@YourActivityActivity, YourActivityShareYourTrainingActivity::class.java)
            startActivity(intent)
        }
        expandPlotYourActivityImage = findViewById(R.id.angle_down_2)
        expandPlotYourActivityImage.setOnClickListener {
            val intent = Intent(this@YourActivityActivity, YourActivityPlotYourActivityActivity::class.java)
            startActivity(intent)
        }
    }
}