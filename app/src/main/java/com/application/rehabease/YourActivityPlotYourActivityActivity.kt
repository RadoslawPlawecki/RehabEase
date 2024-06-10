package com.application.rehabease

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class YourActivityPlotYourActivityActivity : AppCompatActivity() {
    private lateinit var openMenu: ImageView
    private lateinit var expandShareYourTrainingImage: ImageView
    private lateinit var collapsePlotYourActivityImage: ImageView
    private lateinit var goToPlotYourActivityImage: ImageView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_activity_plot_your_activity)
        openMenu = findViewById(R.id.image_bars)
        openMenu.setOnClickListener {
            val intent = Intent(this@YourActivityPlotYourActivityActivity, MenuActivity::class.java)
            startActivity(intent)
        }
        expandShareYourTrainingImage = findViewById(R.id.angle_down_1)
        expandShareYourTrainingImage.setOnClickListener {
            val intent = Intent(this@YourActivityPlotYourActivityActivity, YourActivityShareYourTrainingActivity::class.java)
            startActivity(intent)
        }
        collapsePlotYourActivityImage = findViewById(R.id.angle_up_2)
        collapsePlotYourActivityImage.setOnClickListener {
            finish()
        }
        goToPlotYourActivityImage = findViewById(R.id.right_arrow)
        goToPlotYourActivityImage.setOnClickListener {
            val intent = Intent(this@YourActivityPlotYourActivityActivity, PlotYourActivityActivity::class.java)
            startActivity(intent)
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }
}