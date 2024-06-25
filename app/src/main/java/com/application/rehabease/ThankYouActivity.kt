package com.application.rehabease

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.application.rehabease.user.DashboardActivityUser

class ThankYouActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thank_you)

        val feedbackMessage = intent.getStringExtra("FEEDBACK_MESSAGE")
        val thankFeedbackTextView: TextView = findViewById(R.id.thank_feedback)
        thankFeedbackTextView.text = feedbackMessage

        Handler(Looper.myLooper()!!).postDelayed({
            val intent = Intent(this, DashboardActivityUser::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}