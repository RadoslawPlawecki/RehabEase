package com.application.rehabease

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.adapters.TrainingsRecyclerViewAdapter
import com.application.common.ActivityUtils
import com.application.customization.DialogActivity
import com.application.models.TrainingModel
import com.application.service.TrainingsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrainingEvaluationActivity : AppCompatActivity() {
    private var trainingsService: TrainingsService = TrainingsService()
    private var trainingModels: ArrayList<TrainingModel> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrainingsRecyclerViewAdapter
    private lateinit var externalProgressBar: ProgressBar
    private lateinit var internalProgressBar: ProgressBar
    private lateinit var loadingDataTextView: TextView
    private lateinit var legendImageView: ImageView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_evaluation)
        ActivityUtils.actionBarSetup(this)
        legendImageView = findViewById(R.id.legend_image)
        externalProgressBar = findViewById(R.id.external_progress_bar)
        internalProgressBar = findViewById(R.id.internal_progress_bar)
        loadingDataTextView = findViewById(R.id.loading_data_text)
        legendImageView.setOnClickListener {
            displayLegend()
        }
        setupTrainingModels()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupTrainingModels() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val ids = trainingsService.fetchIds()
                val dates = trainingsService.fetchDates()
                val durations = trainingsService.fetchDurations()
                val ratings = trainingsService.fetchRatings()
                val newTrainingModels = ArrayList<TrainingModel>()
                for (i in 0..<ids.size) {
                    newTrainingModels.add(TrainingModel(ids[i], dates[i], durations[i], ratings[i]))
                }
                trainingModels.clear()
                trainingModels.addAll(newTrainingModels)
                setupRecyclerView()
                closeLoading()
                Log.d("TrainingEvaluationActivity", "$trainingModels")
            } catch (e: Exception) {
                e.printStackTrace()
                closeLoading()
            }
        }
    }

    private fun deleteActivity(trainingId: Number) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    trainingsService.removeTraining(trainingId)
                }
                adapter.removeItem(trainingId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view)
        adapter = TrainingsRecyclerViewAdapter(this, trainingModels) { trainingId ->
            deleteActivity(trainingId)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun displayLegend() {
        val dialog = DialogActivity(this, R.layout.dialog_training_legend)
        val view = dialog.getDialog()
        val returnBackButton = view.findViewById<Button>(R.id.legend_button)
        returnBackButton.setOnClickListener {
            dialog.closeDialog()
        }
    }

    private fun closeLoading() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                loadingDataTextView.visibility = View.GONE
                internalProgressBar.visibility = View.GONE
                externalProgressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                legendImageView.visibility = View.VISIBLE
                recyclerView.startAnimation(fadeIn)
                legendImageView.startAnimation(fadeIn)
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
        loadingDataTextView.startAnimation(fadeOut)
        internalProgressBar.startAnimation(fadeOut)
        externalProgressBar.startAnimation(fadeOut)
    }
}