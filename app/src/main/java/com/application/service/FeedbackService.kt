package com.application.service

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FeedbackService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun addFeedback(userId: String, category: String, rating: String) {
        val feedbackRef = db.collection("feedback").document(userId).collection("dailyFeedback")
        val feedbackData = hashMapOf(
            "date" to getCurrentDate(),
            "category" to category,
            "rating" to rating
        )

        try {
            feedbackRef.add(feedbackData).await()
            Log.d("FeedbackService", "Feedback added successfully! Category: $category, Rating: $rating")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("FeedbackService", "Error adding feedback: ${e.message}")
        }
    }

    suspend fun getUserPreferences(userId: String): Map<String, Int> {
        val feedbackRef = db.collection("feedback").document(userId).collection("dailyFeedback")
        val preferences = mutableMapOf<String, Int>()

        try {
            val documentSnapshot = feedbackRef.get().await()
            if (!documentSnapshot.isEmpty) {
                for (document in documentSnapshot.documents) {
                    val category = document.getString("category")
                    val rating = document.getString("rating")
                    if (category != null && rating == "good") {
                        preferences[category] = preferences.getOrDefault(category, 0) + 1
                    }
                }
            } else {
                Log.e("FeedbackService", "No feedback documents found!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("FeedbackService", "Error fetching user preferences: ${e.message}")
        }

        return preferences
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
}