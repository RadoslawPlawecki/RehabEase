package com.application.rehabease

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class ThankYouActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thank_you)
        Handler(Looper.myLooper()!!).postDelayed( {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }, 3000)
    }
}