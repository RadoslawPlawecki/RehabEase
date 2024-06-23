package com.application.models

class InjuryModel(private var name: String, private var description: String) {
    fun getName(): String {
        return name
    }
    fun getDescription(): String {
        return description
    }
}