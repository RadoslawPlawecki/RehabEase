package com.application.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.models.ExerciseModel
import com.application.rehabease.R

class ExercisesRecyclerViewAdapter(private val context: Context, private val exerciseModels: ArrayList<ExerciseModel>, private val onDeleteCLick: (String) -> Unit) : RecyclerView.Adapter<ExercisesRecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.exercise_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ExercisesRecyclerViewAdapter.MyViewHolder,
        position: Int
    ) {
        holder.bind(exerciseModels[position].getName(), exerciseModels[position].getDescription(), exerciseModels[position].getRepetitions(), exerciseModels[position].getCycles(), exerciseModels[position].getRepetitionTime())
    }

    override fun getItemCount(): Int {
        return exerciseModels.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var exerciseNameTextView: TextView = itemView.findViewById(R.id.exercise_name_text)
        private var exerciseDescriptionTextView: TextView = itemView.findViewById(R.id.exercise_description_text)
        private var exerciseParametersTextView: TextView = itemView.findViewById(R.id.exercise_parameters_text)
        private var deleteImageView: ImageView = itemView.findViewById(R.id.cross_image)

        @SuppressLint("SetTextI18n")
        fun bind(name: String, description: String, repetitions: Number, cycles: Number, repetitionTime: Number) {
            exerciseNameTextView.text = name
            exerciseDescriptionTextView.text = description
            exerciseParametersTextView.text = "Repetitions: ${repetitions.toInt()} | Cycles: ${cycles.toInt()} | Time per repetition: ${repetitionTime.toInt()} sec"
            deleteImageView.setOnClickListener {
                onDeleteCLick(name)
            }
        }
    }

    fun removeItem(exerciseName: String) {
        var positionToRemove = -1
        for ((index, model) in exerciseModels.withIndex()) {
            if (model.getName() == exerciseName) {
                positionToRemove = index
                break
            }
        }
        if (positionToRemove != -1) {
            exerciseModels.removeAt(positionToRemove)
            notifyItemRemoved(positionToRemove)
        }
    }

    fun addItem(exerciseName: String, exerciseDescription: String, exerciseRepetitions: Number, exerciseCycles: Number, exerciseRepetitionTime: Number) {
        exerciseModels.add(ExerciseModel(exerciseName, exerciseDescription, exerciseRepetitions, exerciseCycles, exerciseRepetitionTime))
        notifyItemInserted(exerciseModels.size - 1)
    }
}