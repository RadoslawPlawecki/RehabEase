package com.application.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.models.InjuryModel
import com.application.rehabease.R

class InjuriesRecyclerViewAdapter(private val context: Context, private val injuryModels: ArrayList<InjuryModel>, private val onDeleteCLick: (String) -> Unit) : RecyclerView.Adapter<InjuriesRecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.injury_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: InjuriesRecyclerViewAdapter.MyViewHolder,
        position: Int
    ) {
        holder.bind(injuryModels[position].getName(), injuryModels[position].getDescription())
    }

    override fun getItemCount(): Int {
        return injuryModels.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var injuryNameTextView: TextView = itemView.findViewById(R.id.injury_name_text)
        private var injuryDescriptionTextView: TextView = itemView.findViewById(R.id.injury_description_text)
        private var deleteImageView: ImageView = itemView.findViewById(R.id.cross_image)

        @SuppressLint("SetTextI18n")
        fun bind(name: String, description: String) {
            injuryNameTextView.text = name
            injuryDescriptionTextView.text = description
            deleteImageView.setOnClickListener {
                onDeleteCLick(name)
            }
        }
    }

    fun removeItem(exerciseName: String) {
        var positionToRemove = -1
        for ((index, model) in injuryModels.withIndex()) {
            if (model.getName() == exerciseName) {
                positionToRemove = index
                break
            }
        }
        if (positionToRemove != -1) {
            injuryModels.removeAt(positionToRemove)
            notifyItemRemoved(positionToRemove)
        }
    }

    fun addItem(injuryName: String, injuryDescription: String) {
        injuryModels.add(InjuryModel(injuryName, injuryDescription))
        notifyItemInserted(injuryModels.size - 1)
    }
}