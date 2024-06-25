package com.application.rehabease.user

import ExercisesFeedbackService
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.application.common.ActivityUtils
import com.application.rehabease.R
import com.application.rehabease.ThankYouActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdjustExerciseFeedbackSubmitActivityUser : AppCompatActivity() {
    private var exerciseName: String? = null
    private var exercisesFeedbackService: ExercisesFeedbackService = ExercisesFeedbackService()
    private lateinit var submitFeedbackButton: Button
    private lateinit var repetitionsRating: RatingBar
    private lateinit var cyclesRating: RatingBar
    private lateinit var repetitionTimeRating: RatingBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adjust_exercise_user_feedback_submit)
        ActivityUtils.actionBarSetup(this)
        exerciseName = intent.getStringExtra("EXERCISE_NAME")
        repetitionsRating = findViewById(R.id.repetitions_rating_value)
        cyclesRating = findViewById(R.id.cycles_rating_value)
        repetitionTimeRating = findViewById(R.id.repetition_time_rating_value)
        submitFeedbackButton = findViewById(R.id.submit_feedback_button)
        submitFeedbackButton.setOnClickListener {
            if (!exerciseName.isNullOrEmpty()) {
                val repetitionsRatingValue = repetitionsRating.rating
                val cyclesRatingValue = cyclesRating.rating
                val repetitionTimeRatingValue = repetitionTimeRating.rating
                CoroutineScope(Dispatchers.Main).launch {
                    exercisesFeedbackService.addFeedback(exerciseName!!, repetitionsRatingValue, cyclesRatingValue, repetitionTimeRatingValue)
                }
                val intent = Intent(this, ThankYouActivity::class.java)
                intent.putExtra("TITLE", "Adjust Exercise")
                startActivity(intent)
            } else {
                Toast.makeText(this, "No exercise name provided!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
