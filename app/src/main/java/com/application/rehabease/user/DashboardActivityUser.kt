package com.application.rehabease.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.application.common.ActivityUtils
import com.application.other.DateOperations
import com.application.rehabease.R
import com.application.rehabease.TipActivity
import com.application.rehabease.TrainingEvaluationActivity
import com.application.rehabease.YourActivityActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DashboardActivityUser : AppCompatActivity() {
    private lateinit var helloTextView: TextView
    private lateinit var daysOfUseTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_user)
        helloTextView = findViewById(R.id.hello)
        daysOfUseTextView = findViewById(R.id.days_of_use)
        ActivityUtils.actionBarSetup(this)
        ActivityUtils.changeActivity<ImageView>(R.id.tip_image, this, TipActivity())
        ActivityUtils.changeActivity<Button>(R.id.button_treatment_details, this, TreatmentDetailsActivityUser())
        ActivityUtils.changeActivity<Button>(R.id.button_your_activity, this, YourActivityActivity())
        ActivityUtils.changeActivity<Button>(R.id.button_adjust_exercise, this, AdjustExerciseActivityUser())
        ActivityUtils.changeActivity<Button>(R.id.button_training_evaluation, this, TrainingEvaluationActivity())
        CoroutineScope(Dispatchers.Main).launch {
            helloUser()
            displayUserStreak()
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

    private suspend fun displayUserStreak() {
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
                    val lastLoginDate = documentSnapshot.getString("lastLoginDate")
                    val streak = documentSnapshot.getLong("streak")?.toInt() ?: 0
                    val currentDate = DateOperations.getCurrentDate()
                    val dateDiffInDays = DateOperations.getDateDifference(lastLoginDate.toString(), currentDate)
                    val updatedStreak = when (dateDiffInDays) {
                        0.toLong() -> {
                            streak
                        }
                        1.toLong() -> {
                            streak + 1
                        }
                        else -> {
                            1
                        }
                    }
                    if (lastLoginDate != currentDate) {
                        userRef.update("lastLoginDate", currentDate, "streak", updatedStreak).await()
                    }
                    val streakText = if (updatedStreak != 1) {
                        getString(R.string.you_ve_been_using_rehabease_for_x_days_in_a_row, updatedStreak)
                    } else {
                        getString(R.string.you_ve_been_using_rehabease_for_1_day, updatedStreak)
                    }
                    daysOfUseTextView.text = streakText
                } else {
                    val initialStreak = 1
                    val currentDate = DateOperations.getCurrentDate()
                    userRef.set(mapOf("lastLoginDate" to currentDate, "streak" to initialStreak)).await()
                    val streakText = getString(R.string.you_ve_been_using_rehabease_for_1_day, initialStreak)
                    daysOfUseTextView.text = streakText
                }
            } catch (e: Exception) {
                daysOfUseTextView.text = getString(R.string.you_ve_been_using_rehabease_for_1_day, 1)
                e.printStackTrace()
            }
        }
    }
}