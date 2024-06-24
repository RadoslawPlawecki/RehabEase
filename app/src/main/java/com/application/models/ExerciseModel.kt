package com.application.models

class ExerciseModel(private var name: String, private var description: String, private var repetitions: Number, private var cycles: Number, private var repetitionTime: Number) {
    fun getName(): String {
        return name
    }
    fun getDescription(): String {
        return description
    }
    fun getRepetitions(): Number {
        return repetitions
    }
    fun getCycles(): Number {
        return cycles
    }
    fun getRepetitionTime(): Number {
        return repetitionTime
    }
}