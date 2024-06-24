package com.application.service

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class InjuriesService {
    suspend fun fetchInjuryDetails(field: String): ArrayList<String> {
        val injuryDetails = ArrayList<String>()
        val db = FirebaseFirestore.getInstance()
        val injuriesRef = db.collection("injuries").orderBy("id")
        try {
            val documentSnapshot = injuriesRef.get().await()
            if (!documentSnapshot.isEmpty) {
                for (document in documentSnapshot.documents) {
                    val detail = document.getString(field)
                    detail?.let {
                        injuryDetails.add(it)
                    }
                }
            } else {
                Log.e("InjuriesService", "The document is empty!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return injuryDetails
    }

    suspend fun addInjury(name: String, description: String) {
        val db = FirebaseFirestore.getInstance()
        val exercisesRef = db.collection("injuries")
        val exercise = hashMapOf(
            "name" to name,
            "description" to description
        )
        try {
            exercisesRef.add(exercise).await()
            Log.d("InjuriesService", "Injury added successfully!")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("InjuriesService", "Error adding injury: ${e.message}")
        }
    }

    suspend fun removeInjury(injuryName: String) {
        val db = FirebaseFirestore.getInstance()
        val activitiesRef = db.collection("injuries")
        try {
            val querySnapshot = activitiesRef.whereEqualTo("name", injuryName).get().await()
            if (!querySnapshot.isEmpty) {
                for (document in querySnapshot.documents) {
                    document.reference.delete().await()
                    Log.d("InjuriesService", "Injury removed successfully!")
                }
            } else {
                Log.e("InjuriesService", "No injury found with the given name")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}