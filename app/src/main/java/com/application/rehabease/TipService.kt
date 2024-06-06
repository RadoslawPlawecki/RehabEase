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
import kotlin.random.Random

object TipService {

    private const val OPENAI_API_URL = "https://api.openai.com/v1/chat/completions"
    private const val OPENAI_API_KEY = " "

    private val prompts = listOf(

        "Share a simple rehabilitation exercise for limb recovery. Keep it within 300 tokens.",

        "Give a daily tip for improving limb mobility. Maximum 300 tokens.",

        "Provide a practical tip for joint flexibility during rehabilitation. Max 300 tokens.",

        "Suggest an arm exercise to help with rehabilitation. Limit to 300 tokens.",

        "Share a leg strengthening tip for rehab patients. Keep it to 300 tokens.",

        "Give a tip on safely increasing exercise intensity during rehabilitation. 300 tokens max.",

        "Provide a home exercise tip for maintaining fitness. 300 tokens or less.",

        "Share a simple stretch that can be done daily. Limited to 300 tokens.",

        "Suggest a beginner-friendly balance exercise. Max 300 tokens.",

        "Give a fitness tip for building core strength at home. 300 tokens max.",

        "Give a daily nutrition tip for a balanced diet. Keep it within 300 tokens.",

        "Provide a tip on healthy snacks for energy. Max 300 tokens.",

        "Share a diet tip for improving muscle recovery. Limited to 300 tokens.",

        "Suggest a healthy breakfast idea to start the day strong. 300 tokens max.",

        "Give a smoothie recipe that supports recovery. Keep it to 300 tokens.",

        "Provide a tip for improving sleep quality. 300 tokens max.",

        "Share a daily habit to promote better sleep. Keep it within 300 tokens.",

        "Offer advice on creating a bedtime routine for better rest. Max 300 tokens.",

        "Suggest a relaxation technique before sleep. Limited to 300 tokens.",

        "Give a mental health tip for keeping a positive mindset. 300 tokens max.",

        "Provide advice on coping with stress. Keep it within 300 tokens.",

        "Share a simple mindfulness exercise. Max 300 tokens.",

        "Suggest a daily habit for mental well-being. Limited to 300 tokens.",

        "Offer a tip on setting and achieving small goals. 300 tokens max.",

        "Provide a hydration tip for maintaining good health. 300 tokens or less.",

        "Share a tip on how much water to drink daily. Max 300 tokens.",

        "Suggest an easy way to remember to stay hydrated. 300 token limit.",

        "Give a daily tip for maintaining good posture. Keep it within 300 tokens.",

        "Provide advice on setting up an ergonomic workspace. Max 300 tokens.",

        "Suggest a posture correction exercise. Limited to 300 tokens.",

        "Give a tip for a healthy lifestyle change. Keep it to 300 tokens.",

        "Provide advice on incorporating small healthy habits. Max 300 tokens.",

        "Share a daily habit that promotes overall health. Limited to 300 tokens.",

        "Suggest a simple lifestyle change for better health. 300 tokens max.",

        "Provide a self-care tip for daily practice. Keep it within 300 tokens.",

        "Share a tip on how to take short breaks throughout the day. Max 300 tokens.",

        "Suggest a self-care activity to do at home. Limited to 300 tokens.",

        "Give a daily tip on practicing gratitude. 300 tokens max.",

        "Share a healthy cooking tip for rehabilitation patients. Keep it to 300 tokens.",

        "Give a quick and healthy meal idea. Max 300 tokens.",

        "Suggest a simple recipe for improving nutrition. Limited to 300 tokens.",

        "Provide a cooking tip for preparing nutritious meals. 300 tokens max."

    )

    suspend fun getTipOfTheDay(): String {
        val prompt = getRandomPrompt()
        return fetchTipFromOpenAI(prompt)
    }


    private fun getRandomPrompt(): String {
        return prompts[Random.nextInt(prompts.size)]
    }


    private suspend fun fetchTipFromOpenAI(prompt: String): String = withContext(Dispatchers.IO) {

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
            Log.d("TipService", "Response code: ${response.code}")
            Log.d("TipService", "Response message: ${response.message}")
            Log.d("TipService", "Response body: $responseBody")

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
            Log.e("TipService", "API call failed", e)
            "IOException: ${e.message}"
        }
    }

}