package com.application.rehabease

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    private lateinit var openMenu: ImageView
    private lateinit var lightBulb: ImageView
    private lateinit var treatmentDetailsButton: Button
    private lateinit var yourActivityButton: Button
    private lateinit var adjustExerciseButton: Button
    private lateinit var trainingEvaluationButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        openMenu = findViewById(R.id.image_bars)
        openMenu.setOnClickListener {
            val intent = Intent(this@DashboardActivity, MenuActivity::class.java)
            startActivity(intent)
        }
        lightBulb = findViewById(R.id.tip)
        lightBulb.setOnClickListener {
            val intent = Intent(this@DashboardActivity, TipActivity::class.java)
            startActivity(intent)
        }
        treatmentDetailsButton = findViewById(R.id.button_treatment_details)
        treatmentDetailsButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, TreatmentDetailsActivity::class.java)
            startActivity(intent)
        }
        yourActivityButton = findViewById(R.id.button_your_activity)
        yourActivityButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, YourActivityActivity::class.java)
            startActivity(intent)
        }
        adjustExerciseButton = findViewById(R.id.button_adjust_exercise)
        adjustExerciseButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, AdjustExerciseActivity::class.java)
            startActivity(intent)
        }
        trainingEvaluationButton = findViewById(R.id.button_training_evaluation)
        trainingEvaluationButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, TrainingEvaluationActivity::class.java)
            startActivity(intent)
        }
    }
}