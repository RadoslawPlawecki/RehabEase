package com.application.rehabease.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.common.ActivityUtils
import com.application.rehabease.R

class AdjustExerciseActivityUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adjust_exercise_user)
        ActivityUtils.actionBarSetup(this)
    }
}