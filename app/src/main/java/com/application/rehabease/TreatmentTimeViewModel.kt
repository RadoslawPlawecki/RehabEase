package com.application.rehabease

import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope

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