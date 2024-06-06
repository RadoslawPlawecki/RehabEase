package com.application.rehabease



import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch



class TipViewModel : ViewModel() {
    private val _tipLiveData = MutableLiveData<String>()
    val tipLiveData: LiveData<String> get() = _tipLiveData

    fun fetchTipOfTheDay() {
        viewModelScope.launch {
            val tip = TipService.getTipOfTheDay()
            _tipLiveData.postValue(tip)
        }
    }

}