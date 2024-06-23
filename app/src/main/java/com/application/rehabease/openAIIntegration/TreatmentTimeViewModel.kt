package com.application.rehabease.openAIIntegration

import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.application.rehabease.openAIIntegration.TreatmentTimeService

import kotlinx.coroutines.launch

class TreatmentTimeViewModel : ViewModel() {
    private val _TreatmentLiveData = MutableLiveData<String>()
    val TreatmentLiveData: LiveData<String> get() = _TreatmentLiveData

    fun fetchTreatmentTime() {
        viewModelScope.launch {
            val treatment = TreatmentTimeService.getTreatmentTime()
            _TreatmentLiveData.postValue(treatment)
        }
    }

}