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
import com.application.adapters.InjuriesRecyclerViewAdapter
import com.application.common.ActivityUtils
import com.application.customization.DialogActivity
import com.application.models.InjuryModel
import com.application.other.StringOperations
import com.application.rehabease.R
import com.application.service.InjuriesService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TreatmentDetailsActivityAdmin: AppCompatActivity() {
    private var injuriesService: InjuriesService = InjuriesService()
    private var injuryModels: ArrayList<InjuryModel> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InjuriesRecyclerViewAdapter
    private lateinit var externalProgressBar: ProgressBar
    private lateinit var internalProgressBar: ProgressBar
    private lateinit var loadingDataTextView: TextView
    private lateinit var addNewImageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_details_admin)
        ActivityUtils.actionBarSetup(this)
        addNewImageView = findViewById(R.id.add_new_injury_image)
        externalProgressBar = findViewById(R.id.external_progress_bar)
        internalProgressBar = findViewById(R.id.internal_progress_bar)
        loadingDataTextView = findViewById(R.id.loading_data_text)
        addNewImageView.setOnClickListener {
            addNewInjury()
        }
        setupInjuryModels()
    }

    private fun addNewInjury() {
        val dialog = DialogActivity(this, R.layout.dialog_add_injury)
        val view = dialog.getDialog()
        val submitButton = view.findViewById<Button>(R.id.submit_button)
        val injuryNameEditText = view.findViewById<EditText>(R.id.injury_name_edit_text)
        val injuryDescriptionEditText = view.findViewById<EditText>(R.id.injury_description_edit_text)
        submitButton.setOnClickListener {
            val injuryName = injuryNameEditText.text.toString()
            val injuryDescription = injuryDescriptionEditText.text.toString()
            if (StringOperations.checkIfEmpty(injuryName, injuryDescription)) {
                Toast.makeText(this, "Enter valid injury details!", Toast.LENGTH_SHORT).show()
            }
                else {
                    if (checkIfInjuryAlreadyExists(injuryName)) {
                        CoroutineScope(Dispatchers.Main).launch {
                            injuriesService.addInjury(injuryName, injuryDescription)
                        }
                        adapter.addItem(injuryName, injuryDescription)
                        Toast.makeText(this, "Added successfully!", Toast.LENGTH_SHORT).show()
                        dialog.closeDialog()
                    } else {
                        Toast.makeText(this, "The injury already exists!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupInjuryModels() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val names = injuriesService.fetchInjuryDetails("name")
                val descriptions = injuriesService.fetchInjuryDetails("description")
                val newInjuryModels = ArrayList<InjuryModel>()
                for (i in 0..<names.size) {
                    newInjuryModels.add(InjuryModel(names[i], descriptions[i]))
                }
                injuryModels.clear()
                injuryModels.addAll(newInjuryModels)
                Log.d("TreatmentDetailsActivityAdmin", "$injuryModels")
                setupRecyclerView()
                closeLoading()
            } catch (e: Exception) {
                e.printStackTrace()
                closeLoading()
            }
        }
    }

    private fun deleteInjury(injuryName: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    injuriesService.removeInjury(injuryName)
                }
                adapter.removeItem(injuryName)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.injury_recycler_view)
        adapter = InjuriesRecyclerViewAdapter(this, injuryModels) { injuryName ->
            deleteInjury(injuryName)
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

    private fun checkIfInjuryAlreadyExists(injuryName: String): Boolean {
        var injuryNotExists = true
        for (i in 0..<injuryModels.size) {
            if (injuryName.trim().lowercase() == injuryModels[i].getName().trim().lowercase()) {
                injuryNotExists = false
            }
        }
        return injuryNotExists
    }
}