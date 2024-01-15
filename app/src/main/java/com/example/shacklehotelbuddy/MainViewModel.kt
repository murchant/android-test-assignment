package com.example.shacklehotelbuddy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {


    data class SearchParameter(
        var adult: Int,
        var children: Int,
        var checkInDate: String,
        var checkOutDate: String,
    )

    val _searchParameters = MutableLiveData(
        SearchParameter(
            0,
            0,
            "",
            ""
        )
    )
    val searchParameters : LiveData<SearchParameter>
        get() =  _searchParameters


    fun updateAdultValue(value: Int) {
        val currentSearchParameter = searchParameters.value
        currentSearchParameter?.adult = value
        _searchParameters.value = currentSearchParameter
        Log.d("MainViewModel", "updateAdultValue: ${_searchParameters.value}")

    }

    fun updateChildrenValue(value: Int) {
        val currentSearchParameter = searchParameters.value
        currentSearchParameter?.children = value
        _searchParameters.value = currentSearchParameter
        Log.d("MainViewModel", "updateChildrenValue: ${_searchParameters.value}")

    }

    fun updateCheckInDate(value: String) {
        val currentSearchParameter = searchParameters.value
        currentSearchParameter?.checkInDate = value
        _searchParameters.value = currentSearchParameter
        Log.d("MainViewModel", "updateCheckInDate: ${_searchParameters.value}")

    }

    fun updateCheckOutDate(value: String) {
        val currentSearchParameter = searchParameters.value
        currentSearchParameter?.checkOutDate = value
        _searchParameters.value = currentSearchParameter
        Log.d("MainViewModel", "updateCheckOutDate: ${_searchParameters.value}")
    }

}