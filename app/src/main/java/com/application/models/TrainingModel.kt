package com.application.models

class TrainingModel(private var id: Number, private var date: String, private var duration: Number, private var rating: Number) {
    fun getId(): Number {
        return id
    }
    fun getDate(): String {
        return date
    }
    fun getDuration(): Number {
        return duration
    }
    fun getRating(): Number {
        return rating
    }
}