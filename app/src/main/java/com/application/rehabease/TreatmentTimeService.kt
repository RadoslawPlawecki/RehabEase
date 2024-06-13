package com.application.rehabease

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object TreatmentTimeService {

    private const val OPENAI_API_URL = "https://api.openai.com/v1/chat/completions"
    private const val OPENAI_API_KEY = ""

    val age = "32yo"
    val injury = "mallet finger"
    val prompt = "I need the answer to be 3 sentences maximum. I'm $age and have a standard type" +
            " $injury, what is the expected rehabilitation time?"


    suspend fun getTreatmentTime(): String {
        return fetchTreatmentTimeFromOpenAI(prompt)
    }



    private suspend fun fetchTreatmentTimeFromOpenAI(prompt: String): String = withContext(Dispatchers.IO) {

        val client = OkHttpClient()
        val requestBodyJson = JSONObject().apply {
            put("model", "gpt-3.5-turbo")
            val messagesArray = JSONArray().apply {
                put(JSONObject().put("role", "user").put("content", prompt))
            }
            put("messages", messagesArray)
            put("temperature", 1)
        }

        val requestBody =
            requestBodyJson.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(OPENAI_API_URL)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $OPENAI_API_KEY")
            .post(requestBody)
            .build()



        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            Log.d("TreatmentTimeService", "Response code: ${response.code}")
            Log.d("TreatmentTimeService", "Response message: ${response.message}")
            Log.d("TreatmentTimeService", "Response body: $responseBody")

            if (response.isSuccessful && responseBody != null) {
                val jsonResponse = JSONObject(responseBody)
                val choicesArray = jsonResponse.getJSONArray("choices")
                if (choicesArray.length() > 0) {
                    val firstChoice = choicesArray.getJSONObject(0)
                    val message = firstChoice.getJSONObject("message")
                    val content = message.getString("content")
                    content
                } else {
                    "No choices found in the response."
                }
            } else {
                "Error: ${response.code}. ${response.message}"
            }
        } catch (e: IOException) {
            Log.e("TreatmentTimeService", "API call failed", e)
            "IOException: ${e.message}"
        }
    }

}