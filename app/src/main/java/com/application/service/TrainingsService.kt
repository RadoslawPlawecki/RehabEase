package com.application.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TrainingsService {
    suspend fun removeTraining(trainingId: Number) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val db = FirebaseFirestore.getInstance()
            val activitiesRef = db.collection("users").document(uid).collection("trainings")
            try {
                val querySnapshot = activitiesRef.whereEqualTo("id", trainingId).get().await()
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        document.reference.delete().await()
                        Log.d("TrainingsService", "Training removed successfully!")
                    }
                } else {
                    Log.e("TrainingService", "No training found with the given id!")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    suspend fun fetchIds(): ArrayList<Number> {
        val ids = ArrayList<Number>()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val db = FirebaseFirestore.getInstance()
            val activitiesRef = db.collection("users").document(uid).collection("trainings").orderBy("date")
            try {
                val documentSnapshot = activitiesRef.get().await()
                if (!documentSnapshot.isEmpty) {
                    for (document in documentSnapshot.documents) {
                        val id = document.getLong("id")
                        id?.let {
                            ids.add(it)
                        }
                    }
                } else {
                    Log.e("TrainingsService", "The document is empty!")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return ids
    }
    suspend fun fetchDates(): ArrayList<String> {
        val dates = ArrayList<String>()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val db = FirebaseFirestore.getInstance()
            val activitiesRef = db.collection("users").document(uid).collection("trainings").orderBy("date")
            try {
                val documentSnapshot = activitiesRef.get().await()
                if (!documentSnapshot.isEmpty) {
                    for (document in documentSnapshot.documents) {
                        val date = document.getString("date")
                        date?.let {
                            dates.add(it)
                        }
                    }
                } else {
                    Log.e("TrainingsService", "The document is empty!")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return dates
    }
    suspend fun fetchDurations(): ArrayList<Number> {
        val durations = ArrayList<Number>()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val db = FirebaseFirestore.getInstance()
            val activitiesRef = db.collection("users").document(uid).collection("trainings").orderBy("date")
            try {
                val documentSnapshot = activitiesRef.get().await()
                if (!documentSnapshot.isEmpty) {
                    for (document in documentSnapshot.documents) {
                        val duration = document.getLong("duration")
                        duration?.let {
                            durations.add(it)
                        }
                    }
                } else {
                    Log.e("TrainingsService", "The document is empty!")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return durations
    }

    suspend fun fetchRatings(): ArrayList<Number> {
        val ratings = ArrayList<Number>()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val db = FirebaseFirestore.getInstance()
            val activitiesRef = db.collection("users").document(uid).collection("trainings").orderBy("date")
            try {
                val documentSnapshot = activitiesRef.get().await()
                if (!documentSnapshot.isEmpty) {
                    for (document in documentSnapshot.documents) {
                        val rating = document.getLong("rating")
                        rating?.let {
                            ratings.add(it)
                        }
                    }
                } else {
                    Log.e("TrainingsService", "The document is empty!")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return ratings
    }
}