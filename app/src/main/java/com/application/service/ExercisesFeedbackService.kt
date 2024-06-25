import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import android.util.Log
import com.application.other.DateOperations
import com.google.firebase.auth.FirebaseAuth

class ExercisesFeedbackService {
    suspend fun addFeedback(exerciseName: String, repetitionsRating: Number, cyclesRating: Number, repetitionTimeRating: Number) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        try {
            val currentUser = auth.currentUser
            val userId = currentUser?.uid
            if (userId != null) {
                val exerciseQuery = db.collection("exercises")
                    .whereEqualTo("name", exerciseName)
                    .limit(1)
                val exerciseSnapshot = exerciseQuery.get().await()
                if (!exerciseSnapshot.isEmpty) {
                    val exerciseDoc = exerciseSnapshot.documents[0]
                    val exerciseId = exerciseDoc.id
                    val userDoc = db.collection("users").document(userId).get().await()
                    if (userDoc.exists()) {
                        val email = userDoc.getString("email")
                        val feedback = hashMapOf(
                            "uid" to userId,
                            "email" to email,
                            "exerciseName" to exerciseName,
                            "repetitionsRating" to repetitionsRating,
                            "cyclesRating" to cyclesRating,
                            "repetitionTimeRating" to repetitionTimeRating,
                            "date" to DateOperations.getCurrentDate()
                        )
                        val exerciseRef = db.collection("exercises").document(exerciseId)
                        exerciseRef.collection("feedback")
                            .add(feedback)
                            .await()
                        Log.d("ExercisesFeedbackService", "Feedback added successfully!")
                    } else {
                        Log.e(
                            "ExercisesFeedbackService",
                            "User document not found for userId: $userId"
                        )
                    }
                } else {
                    Log.e(
                        "ExercisesFeedbackService",
                        "Exercise not found with name: $exerciseName"
                    )
                }
            } else {
                Log.e("ExercisesFeedbackService", "Current user is null")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ExercisesFeedbackService", "Error adding feedback: ${e.message}")
        }
    }
}

