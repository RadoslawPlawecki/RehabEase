package com.application.service

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class InjuriesService {
    suspend fun fetchInjuriesNames(): ArrayList<String> {
        val injuryNames = ArrayList<String>()
        val db = FirebaseFirestore.getInstance()
        val injuriesRef = db.collection("injuries").orderBy("id")
        try {
            val documentSnapshot = injuriesRef.get().await()
            if (!documentSnapshot.isEmpty) {
                for (document in documentSnapshot.documents) {
                    val injuryName = document.getString("name")
                    injuryName?.let {
                        injuryNames.add(it)
                    }
                }
            } else {
                Log.e("InjuriesService", "The document is empty!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return injuryNames
    }
    suspend fun fetchInjuriesDescriptions(): ArrayList<String> {
        val injuryDescriptions = ArrayList<String>()
        val db = FirebaseFirestore.getInstance()
        val injuriesRef = db.collection("injuries").orderBy("id")
        try {
            val documentSnapshot = injuriesRef.get().await()
            if (!documentSnapshot.isEmpty) {
                for (document in documentSnapshot.documents) {
                    val injuryDescription = document.getString("description")
                    injuryDescription?.let {
                        injuryDescriptions.add(it)
                    }
                }
            } else {
                Log.e("InjuriesService", "The document is empty!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return injuryDescriptions
    }
}