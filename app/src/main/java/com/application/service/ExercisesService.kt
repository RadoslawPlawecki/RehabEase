package com.application.service

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ExercisesService {
    suspend fun fetchExerciseDetailsString(field: String): ArrayList<String> {
        val exerciseDetails = ArrayList<String>()
        val db = FirebaseFirestore.getInstance()
        val exercisesRef = db.collection("exercises")
        try {
            val documentSnapshot = exercisesRef.get().await()
            if (!documentSnapshot.isEmpty) {
                for (document in documentSnapshot.documents) {
                    val detail = document.getString(field)
                    detail?.let {
                        exerciseDetails.add(it)
                    }
                }
            } else {
                Log.e("ExercisesService", "The document is empty!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return exerciseDetails
    }

    suspend fun fetchExerciseDetailsNumber(field: String): ArrayList<Number> {
        val exerciseDetails = ArrayList<Number>()
        val db = FirebaseFirestore.getInstance()
        val exercisesRef = db.collection("exercises")
        try {
            val documentSnapshot = exercisesRef.get().await()
            if (!documentSnapshot.isEmpty) {
                for (document in documentSnapshot.documents) {
                    val detail = document.getDouble(field)
                    detail?.let {
                        exerciseDetails.add(it)
                    }
                }
            } else {
                Log.e("ExercisesService", "The document is empty!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return exerciseDetails
    }

    suspend fun addExercise(name: String, description: String, repetitions: String, cycles: String, repetitionTime: String) {
        val db = FirebaseFirestore.getInstance()
        val exercisesRef = db.collection("exercises")
        val exercise = hashMapOf(
            "name" to name,
            "description" to description,
            "repetitions" to repetitions.toInt(),
            "cycles" to cycles.toInt(),
            "repetitionTime" to repetitionTime.toInt()
        )
        try {
            exercisesRef.add(exercise).await()
            Log.d("ExercisesService", "Exercise added successfully!")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ExercisesService", "Error adding exercise: ${e.message}")
        }
    }

    suspend fun removeExercise(exerciseName: String) {
        val db = FirebaseFirestore.getInstance()
        val activitiesRef = db.collection("exercises")
        try {
            val querySnapshot = activitiesRef.whereEqualTo("name", exerciseName).get().await()
            if (!querySnapshot.isEmpty) {
                for (document in querySnapshot.documents) {
                    document.reference.delete().await()
                    Log.d("ExercisesService", "Exercise removed successfully!")
                }
            } else {
                Log.e("ExercisesService", "No exercise found with the given name")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}