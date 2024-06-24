package com.application.other

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateOperations {
    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getDateDifference(firstDateString: String, secondDateString: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val firstDate = sdf.parse(firstDateString)
        val secondDate = sdf.parse(secondDateString)
        val timeDiff = firstDate!!.time - secondDate!!.time
        return kotlin.math.abs(timeDiff / (1000 * 60 * 60 * 24)) % 365 // calculate difference in days
    }

    fun getDatesFromCurrent(returnBack: Number): ArrayList<String> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val datesList = ArrayList<String>()
        for (i in 0 until returnBack.toInt()) {
            datesList.add(sdf.format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }
        datesList.reverse()
        return datesList
    }
}