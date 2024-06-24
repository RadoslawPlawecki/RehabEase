package com.application.other

object ArrayListOperations {
    fun getMaxValue(numberList: ArrayList<Number>): Double? {
        if (numberList.isEmpty()) {
            return null
        }
        var max: Number = numberList[0]
        for (number in numberList) {
            if (number.toDouble() > max.toDouble()) {
                max = number
            }
        }
        return max.toDouble()
    }
}