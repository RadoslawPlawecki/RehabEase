package com.application.rehabease.both

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.NetworkType
import com.application.common.ActivityUtils
import com.application.rehabease.R
import com.application.rehabease.TrainingEvaluationActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.application.rehabease.notifications.NotificationWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

class TitleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)
        scheduleDailyNotificationWorker(17, 40)
        ActivityUtils.changeActivity<Button>(R.id.getting_started, this, RegisterActivity())
    }
    private fun scheduleDailyNotificationWorker(hour: Int, minute: Int) {
        val currentDateTime = Calendar.getInstance()
        val scheduledDateTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (scheduledDateTime.before(currentDateTime)) {
            scheduledDateTime.add(Calendar.DAY_OF_MONTH, 1)
        }

    val initialDelay = scheduledDateTime.timeInMillis - currentDateTime.timeInMillis

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresCharging(false)
        .setRequiresBatteryNotLow(true)
        .build()

    val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(this).enqueue(workRequest)
    }
}