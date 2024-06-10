package com.application.rehabease

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class TrainingEvaluationActivity : AppCompatActivity() {
    private lateinit var openMenu: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_evaluation)
        openMenu = findViewById(R.id.image_bars)
        openMenu.setOnClickListener {
            val intent = Intent(this@TrainingEvaluationActivity, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}