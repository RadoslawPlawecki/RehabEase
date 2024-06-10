package com.application.rehabease

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DashboardActivity : AppCompatActivity() {
    private lateinit var openMenu: ImageView
    private lateinit var lightBulb: ImageView
    private lateinit var treatmentDetailsButton: Button
    private lateinit var yourActivityButton: Button
    private lateinit var adjustExerciseButton: Button
    private lateinit var trainingEvaluationButton: Button
    private lateinit var helloTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        helloTextView = findViewById(R.id.hello)
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
        CoroutineScope(Dispatchers.Main).launch {
            helloUser()
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun helloUser() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(uid)
            try {
                val documentSnapshot = withContext(Dispatchers.IO) {
                    userRef.get().await()
                }
                if (documentSnapshot.exists()) {
                    val name = documentSnapshot.getString("name") ?: "User"
                    helloTextView.text = "Hello $name!"
                } else {
                    helloTextView.text = "Hello User!"
                }
            } catch (e: Exception) {
                helloTextView.text = "Hello User!"
                e.printStackTrace()
            }
        }
    }
}