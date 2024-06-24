package com.application.other

import com.application.constants.ActivityComment
import com.application.constants.ActivityCommentTitle

object AssessValues {
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
    fun evaluateActivity(duration: Double): List<String> {
        return when {
            duration >= 40 -> listOf(ActivityCommentTitle.titles[2], ActivityComment.comments[2])
            duration >= 20 -> listOf(ActivityCommentTitle.titles[1], ActivityComment.comments[1])
            else -> listOf(ActivityCommentTitle.titles[0], ActivityComment.comments[0])
        }
    }
}