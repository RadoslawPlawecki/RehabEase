package com.application.rehabease.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.application.common.ActivityUtils
import com.application.customization.CustomSpinner
import com.application.customization.DialogActivity
import com.application.models.ExerciseModel
import com.application.rehabease.R
import com.application.service.ExercisesService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdjustExerciseActivityUser : AppCompatActivity(), CustomSpinner.OnSpinnerEventsListener {
    private var exercisesService: ExercisesService = ExercisesService()
    private var exerciseModels: ArrayList<ExerciseModel> = ArrayList()
    private lateinit var spinner: CustomSpinner
    private lateinit var adapter: ArrayAdapter<CharSequence>
    private lateinit var horizontalProgressBar: ProgressBar
    private lateinit var displayDescriptionImageView: ImageView
    private lateinit var parametersTitleTextView: TextView
    private lateinit var parametersTextView: TextView
    private lateinit var goToFeedbackButton: Button
    private var exerciseName: String = null.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adjust_exercise_user)
        ActivityUtils.actionBarSetup(this)
        goToFeedbackButton = findViewById(R.id.go_to_feedback_exercise_button)
        goToFeedbackButton.setOnClickListener {
            val intent = Intent(this, AdjustExerciseFeedbackSubmitActivityUser::class.java)
            intent.putExtra("EXERCISE_NAME", exerciseName)
            startActivity(intent)
        }
        horizontalProgressBar = findViewById(R.id.horizontal_progress_bar)
        parametersTitleTextView = findViewById(R.id.exercise_parameters_title_text)
        parametersTextView = findViewById(R.id.exercise_parameters_text)
        spinner = findViewById(R.id.exercises)
        spinner.setSpinnerEventsListener(this)
        setupExerciseModels()
        displayDescriptionImageView = findViewById(R.id.more_info_image)
        displayDescriptionImageView.setOnClickListener {
            displayExercise()
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                setParameters(position)
                exerciseName = exerciseModels[position].getName()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // do nothing
            }
        }
    }

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
                setupSpinner(names)
                exerciseModels.clear()
                exerciseModels.addAll(newExerciseModels)
                Log.d("AdjustExerciseActivityUser", "$exerciseModels")
                closeLoading()
            } catch (e: Exception) {
                e.printStackTrace()
                closeLoading()
            }
        }
    }

    private fun setupSpinner(exerciseNames: List<String>) {
        adapter = ArrayAdapter(this, R.layout.my_selected_item, exerciseNames)
        adapter.setDropDownViewResource(R.layout.simple_spinner_item)
        spinner.adapter = adapter
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onPopupWindowOpened(spinner: CustomSpinner) {
        spinner.background = getDrawable(R.drawable.bg_spinner_arrow_up)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onPopupWindowClosed(spinner: CustomSpinner) {
        spinner.background = getDrawable(R.drawable.bg_spinner_arrow_down)
    }

    private fun displayExercise() {
        val dialog = DialogActivity(this, R.layout.dialog_injury_description)
        val view = dialog.getDialog()
        val injuryNameTextView = view.findViewById<TextView>(R.id.injury_name_text)
        val injuryDescriptionTextView = view.findViewById<TextView>(R.id.injury_description_text)
        val selectedPosition = spinner.selectedItemPosition
        injuryNameTextView.text = exerciseModels[selectedPosition].getName()
        injuryDescriptionTextView.text = exerciseModels[selectedPosition].getDescription()
        val returnBackButton = view.findViewById<Button>(R.id.legend_button)
        returnBackButton.setOnClickListener {
            dialog.closeDialog()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setParameters(position: Number) {
        val repetitions = exerciseModels[position.toInt()].getRepetitions()
        val cycles = exerciseModels[position.toInt()].getCycles()
        val repetitionTime = exerciseModels[position.toInt()].getRepetitionTime()
        parametersTextView.text = "Repetitions: ${repetitions.toInt()} \nCycles: ${cycles.toInt()} \nTime per " +
                "repetition: ${repetitionTime.toInt()}"
    }

    private fun closeLoading() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                horizontalProgressBar.visibility = View.GONE
                parametersTitleTextView.visibility = View.VISIBLE
                parametersTextView.visibility = View.VISIBLE
                goToFeedbackButton.visibility = View.VISIBLE
                parametersTitleTextView.startAnimation(fadeIn)
                parametersTextView.startAnimation(fadeIn)
                goToFeedbackButton.startAnimation(fadeIn)
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
        horizontalProgressBar.startAnimation(fadeOut)
    }
}