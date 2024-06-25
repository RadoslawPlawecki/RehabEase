package com.application.rehabease.ServiceOpenAIAPI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TreatmentTimeViewModel : ViewModel() {
    private val _TreatmentLiveData = MutableLiveData<String>()
    val TreatmentLiveData: LiveData<String> get() = _TreatmentLiveData

    fun fetchTreatmentTime(injury: String, age: Int) {
        viewModelScope.launch {
            val treatment = TreatmentTimeService.getTreatmentTime(injury, age)
            _TreatmentLiveData.postValue(treatment)
        }
    }
}