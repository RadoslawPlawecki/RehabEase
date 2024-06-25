package com.application.other

object StringOperations {
    fun checkIfEmpty(vararg words: String): Boolean {
        for (word in words) {
            if (word.trim().isEmpty()) {
                return true
            }
        }
        return false
    }
}