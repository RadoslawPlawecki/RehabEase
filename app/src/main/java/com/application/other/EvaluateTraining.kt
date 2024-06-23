package com.application.other

object EvaluateTraining {
    fun convertToMark(percentile: Double): String {
        return when {
            percentile >= 97 -> "A+"
            percentile >= 93 -> "A"
            percentile >= 90 -> "A-"
            percentile >= 87 -> "B+"
            percentile >= 83 -> "B"
            percentile >= 80 -> "B-"
            percentile >= 77 -> "C+"
            percentile >= 73 -> "C"
            percentile >= 70 -> "C-"
            percentile >= 67 -> "D+"
            percentile >= 63 -> "D"
            percentile >= 60 -> "D-"
            else -> "F"
        }
    }
}