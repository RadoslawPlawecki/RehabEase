package com.application.rehabease

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class YourActivityShareYourTrainingActivity : AppCompatActivity() {
    private lateinit var openMenu: ImageView
    private lateinit var expandPlotYourActivityImage: ImageView
    private lateinit var collapseShareYourTrainingImage: ImageView
    private lateinit var goToShareYourTrainingImage: ImageView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_activity_share_your_training)
        openMenu = findViewById(R.id.image_bars)
        openMenu.setOnClickListener {
            val intent = Intent(this@YourActivityShareYourTrainingActivity, MenuActivity::class.java)
            startActivity(intent)
        }
        expandPlotYourActivityImage = findViewById(R.id.angle_down_2)
        expandPlotYourActivityImage.setOnClickListener {
            val intent = Intent(this@YourActivityShareYourTrainingActivity, YourActivityPlotYourActivityActivity::class.java)
            startActivity(intent)
        }
        goToShareYourTrainingImage = findViewById(R.id.right_arrow)
        goToShareYourTrainingImage.setOnClickListener {
            val intent = Intent(this@YourActivityShareYourTrainingActivity, ShareYourTrainingActivity::class.java)
            startActivity(intent)
        }
        collapseShareYourTrainingImage = findViewById(R.id.angle_up_1)
        collapseShareYourTrainingImage.setOnClickListener {
            finish()
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }
}