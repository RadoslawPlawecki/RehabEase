package com.application.rehabease

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.application.rehabease.user.DashboardActivityUser

class ThankYouActivity: AppCompatActivity() {
    private var title: String? = null
    private lateinit var textTitleTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thank_you)
        textTitleTextView = findViewById(R.id.text_title)
        title = intent.getStringExtra("TITLE")
        textTitleTextView.text = title
        Handler(Looper.myLooper()!!).postDelayed( {
            val intent = Intent(this, DashboardActivityUser::class.java)
            startActivity(intent)
        }, 3000)
    }
}