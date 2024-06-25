package com.application.rehabease.ServiceOpenAIAPI

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.application.service.FeedbackService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class TipViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("TipOfTheDayPrefs", Context.MODE_PRIVATE)
    private val feedbackService = FeedbackService()
    private var userId: String? = null

    private val _tipLiveData = MutableLiveData<String>()
    val tipLiveData: LiveData<String> get() = _tipLiveData

    init {
        fetchUserId()
    }

    private fun fetchUserId() {
        viewModelScope.launch {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                userId = it.uid
            }
        }
    }

    fun fetchTipOfTheDay() {
        viewModelScope.launch {
            val currentDate = getCurrentDate()
            val storedDate = sharedPreferences.getString("tipFetchDate", null)
            val storedTip = sharedPreferences.getString("tipOfTheDay", null)

            if (storedDate == currentDate && storedTip != null) {
                _tipLiveData.postValue(storedTip)
            } else {
                val newTip = fetchTipBasedOnFeedback()
                sharedPreferences.edit().apply {
                    putString("tipOfTheDay", newTip)
                    putString("tipFetchDate", currentDate)
                    apply()
                }
                _tipLiveData.postValue(newTip)
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    suspend fun wasFeedbackGivenToday(): Boolean {
        val currentDate = getCurrentDate()
        val uid = userId ?: return false

        return withContext(Dispatchers.IO) {
            try {
                val db = FirebaseFirestore.getInstance()
                val feedbackRef = db.collection("feedback").document(uid).collection("dailyFeedback")
                val querySnapshot = feedbackRef.whereEqualTo("date", currentDate).get().await()
                return@withContext !querySnapshot.isEmpty
            } catch (e: Exception) {
                Log.e("TipViewModel", "Error checking if feedback was given today: ${e.message}")
                e.printStackTrace()
                return@withContext false
            }
        }
    }

    fun recordFeedback(rating: String) {
        viewModelScope.launch {
            if (wasFeedbackGivenToday()) {
                return@launch // feedback already given today
            }

            userId?.let { uid ->
                val currentTipCategory = sharedPreferences.getString("currentTipCategory", "").also {
                    Log.d("TipViewModel", "Recording feedback. Current tip category: $it")
                } ?: return@launch
                feedbackService.addFeedback(uid, currentTipCategory, rating)

                sharedPreferences.edit().apply {
                    putString("feedbackDate", getCurrentDate())
                    apply()
                }
            } ?: run {
                Log.e("TipViewModel", "User ID is null")
            }
        }
    }

    private suspend fun fetchTipBasedOnFeedback(): String {
        val uid = userId ?: return "Error: User ID not found"
        val userPreferences = feedbackService.getUserPreferences(uid)
        val selectedCategory = getCategoryBasedOnPreferences(userPreferences)
        return TipService.getTipOfTheDay(selectedCategory).also {
            sharedPreferences.edit().apply {
                putString("currentTipCategory", selectedCategory)
                apply()
            }
        }
    }

    private fun getCategoryBasedOnPreferences(preferences: Map<String, Int>): String {
        val weightedCategories = preferences.flatMap { entry ->
            val (category, weight) = entry
            List(weight) { category }
        }

        //no preferences found, default to random
        if (weightedCategories.isEmpty()) {
            val defaultCategories = listOf("exercise", "nutrition", "sleep", "mental_health", "hydration", "posture", "self_care", "cooking")
            val randomCategory = defaultCategories.random()
            Log.d("TipViewModel", "No user preferences found. Selecting random category: $randomCategory")
            return randomCategory
        }

        val selectedCategory = weightedCategories.random()
        Log.d("TipViewModel", "Selected category based on user preferences: $selectedCategory")
        return selectedCategory
    }
}