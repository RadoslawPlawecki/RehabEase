package com.application.rehabease.admin

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.adapters.ExercisesRecyclerViewAdapter
import com.application.common.ActivityUtils
import com.application.customization.DialogActivity
import com.application.models.ExerciseModel
import com.application.other.StringOperations
import com.application.rehabease.R
import com.application.service.ExercisesService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdjustExerciseActivityAdmin : AppCompatActivity() {
    private var exercisesService: ExercisesService = ExercisesService()
    private var exerciseModels: ArrayList<ExerciseModel> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExercisesRecyclerViewAdapter
    private lateinit var externalProgressBar: ProgressBar
    private lateinit var internalProgressBar: ProgressBar
    private lateinit var loadingDataTextView: TextView
    private lateinit var addNewImageView: ImageView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adjust_exercise_admin)
        ActivityUtils.actionBarSetup(this)
        addNewImageView = findViewById(R.id.add_new_image)
        externalProgressBar = findViewById(R.id.external_progress_bar)
        internalProgressBar = findViewById(R.id.internal_progress_bar)
        loadingDataTextView = findViewById(R.id.loading_data_text)
        addNewImageView.setOnClickListener {
            addNewExercise()
        }
        setupExerciseModels()
    }

    private fun addNewExercise() {
        val dialog = DialogActivity(this, R.layout.dialog_add_exercise)
        val view = dialog.getDialog()
        val submitButton = view.findViewById<Button>(R.id.submit_button)
        val exerciseNameEditText = view.findViewById<EditText>(R.id.exercise_name_edit_text)
        val exerciseDescriptionEditText = view.findViewById<EditText>(R.id.exercise_description_edit_text)
        val exerciseRepetitionsEditText = view.findViewById<EditText>(R.id.exercise_repetitions_edit_text)
        val exerciseCyclesEditText = view.findViewById<EditText>(R.id.exercise_cycles_edit_text)
        val exerciseRepetitionTimeEditText = view.findViewById<EditText>(R.id.exercise_repetition_time_edit_text)
        submitButton.setOnClickListener {
            val exerciseName = exerciseNameEditText.text.toString()
            val exerciseDescription = exerciseDescriptionEditText.text.toString()
            val exerciseRepetitions = exerciseRepetitionsEditText.text.toString()
            val exerciseCycles = exerciseCyclesEditText.text.toString()
            val exerciseRepetitionTime = exerciseRepetitionTimeEditText.text.toString()
            if (StringOperations.checkIfEmpty(exerciseName, exerciseDescription, exerciseRepetitions, exerciseCycles, exerciseRepetitionTime)) {
                Toast.makeText(this, "Enter valid exercise details!", Toast.LENGTH_SHORT).show()
            } else {
                if (checkIfExerciseAlreadyExists(exerciseName)) {
                    CoroutineScope(Dispatchers.Main).launch {
                        exercisesService.addExercise(exerciseName, exerciseDescription, exerciseRepetitions, exerciseCycles, exerciseRepetitionTime)
                    }
                    adapter.addItem(exerciseName, exerciseDescription, exerciseRepetitions.toInt(), exerciseCycles.toInt(), exerciseRepetitionTime.toInt())
                    Toast.makeText(this, "Added successfully!", Toast.LENGTH_SHORT).show()
                    dialog.closeDialog()
                } else {
                    Toast.makeText(this, "The exercise already exists!", Toast.LENGTH_SHORT).show()
                    dialog.closeDialog()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupExerciseModels() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val names = exercisesService.fetchExerciseDetailsString("name")
                val descriptions = exercisesService.fetchExerciseDetailsString("description")
                val repetitions = exercisesService.fetchExerciseDetailsNumber("repetitions")
                val cycles = exercisesService.fetchExerciseDetailsNumber("cycles")
                val repetitionTimes = exercisesService.fetchExerciseDetailsNumber("repetitionTime")
                val newExerciseModels = ArrayList<ExerciseModel>()
                for (i in 0..<names.size) {
                    newExerciseModels.add(ExerciseModel(names[i], descriptions[i], repetitions[i], cycles[i], repetitionTimes[i]))
                }
                exerciseModels.clear()
                exerciseModels.addAll(newExerciseModels)
                Log.d("AdjustExerciseActivityAdmin", "$exerciseModels")
                setupRecyclerView()
                closeLoading()
            } catch (e: Exception) {
                e.printStackTrace()
                closeLoading()
            }
        }
    }

    private fun deleteExercise(exerciseName: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    exercisesService.removeExercise(exerciseName)
                }
                adapter.removeItem(exerciseName)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.exercise_recycler_view)
        adapter = ExercisesRecyclerViewAdapter(this, exerciseModels) { exerciseName ->
            deleteExercise(exerciseName)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
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
                addNewImageView.visibility = View.VISIBLE
                recyclerView.startAnimation(fadeIn)
                addNewImageView.startAnimation(fadeIn)
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
        loadingDataTextView.startAnimation(fadeOut)
        internalProgressBar.startAnimation(fadeOut)
        externalProgressBar.startAnimation(fadeOut)
    }

    private fun checkIfExerciseAlreadyExists(exerciseName: String): Boolean {
        var exerciseNotExists = true
        for (i in 0..<exerciseModels.size) {
            if (exerciseName.trim().lowercase() == exerciseModels[i].getName().trim().lowercase()) {
                exerciseNotExists = false
            }
        }
        return exerciseNotExists
    }
}