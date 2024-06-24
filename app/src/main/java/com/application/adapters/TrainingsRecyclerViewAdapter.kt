package com.application.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.models.TrainingModel
import com.application.other.AssessValues
import com.application.rehabease.R

class TrainingsRecyclerViewAdapter(private val context: Context, private val trainingModels: ArrayList<TrainingModel>, private val onDeleteCLick: (Number) -> Unit) : RecyclerView.Adapter<TrainingsRecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.training_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: TrainingsRecyclerViewAdapter.MyViewHolder,
        position: Int
    ) {
        holder.bind(trainingModels[position].getId(), trainingModels[position].getRating(), trainingModels[position].getDate(), trainingModels[position].getDuration())
    }

    override fun getItemCount(): Int {
        return trainingModels.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ratingTextView: TextView = itemView.findViewById(R.id.training_rating_text)
        private var dateTextView: TextView = itemView.findViewById(R.id.training_date_text)
        private var durationTextView: TextView = itemView.findViewById(R.id.training_duration_text)
        private var deleteImageView: ImageView = itemView.findViewById(R.id.trash_image)

        @SuppressLint("SetTextI18n")
        fun bind(id: Number, rating: Number, date: String, duration: Number) {
            ratingTextView.text = AssessValues.convertToMark(rating.toDouble())
            dateTextView.text = date
            durationTextView.text = "$duration minutes"
            deleteImageView.setOnClickListener {
                onDeleteCLick(id)
            }
        }
    }

    fun removeItem(trainingId: Number) {
        var positionToRemove = -1
        for ((index, model) in trainingModels.withIndex()) {
            if (model.getId() == trainingId) {
                positionToRemove = index
                break
            }
        }
        if (positionToRemove != -1) {
            trainingModels.removeAt(positionToRemove)
            notifyItemRemoved(positionToRemove)
        }
    }
}