package com.application.rehabease.both

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.application.common.ActivityUtils
import com.application.rehabease.R
import com.application.rehabease.TrainingEvaluationActivity
import com.application.rehabease.user.AdjustExerciseActivityUser

class TitleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)
        ActivityUtils.changeActivity<Button>(R.id.getting_started, this, RegisterActivity())
    }
}