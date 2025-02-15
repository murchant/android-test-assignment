package com.example.shacklehotelbuddy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shacklehotelbuddy.data.SearchParameter
import com.example.shacklehotelbuddy.data.db.SearchDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchDao: SearchDao
) : ViewModel() {

    private val _recentSearches = MutableLiveData<List<SearchParameter>>()
    val recentSearches: LiveData<List<SearchParameter>>
        get() = _recentSearches

    fun bind() {
        viewModelScope.launch(Dispatchers.IO) {
            val searchParameters = searchDao.getAll()
            _recentSearches.postValue(searchParameters)
        }
    }

    private val _searchParameters = MutableLiveData(
        SearchParameter(
            UUID.randomUUID().toString(),
            0,
            0,
            "",
            ""
        )
    )

    fun resetSearchParameters() {
        _searchParameters.value = SearchParameter(
            UUID.randomUUID().toString(),
            0,
            0,
            "",
            ""
        )
    }

    val searchParameters: LiveData<SearchParameter>
        get() = _searchParameters

    fun updateAdultValue(value: Int) {
        val currentSearchParameter = searchParameters.value
        currentSearchParameter?.adult = value
        currentSearchParameter?.id = UUID.randomUUID().toString()
        _searchParameters.value = currentSearchParameter
        Log.d("MainViewModel", "updateAdultValue: ${_searchParameters.value}")

    }

    fun updateChildrenValue(value: Int) {
        val currentSearchParameter = searchParameters.value
        currentSearchParameter?.children = value
        currentSearchParameter?.id = UUID.randomUUID().toString()
        _searchParameters.value = currentSearchParameter
        Log.d("MainViewModel", "updateChildrenValue: ${_searchParameters.value}")
    }

    fun updateCheckInDate(value: String) {
        val currentSearchParameter = searchParameters.value
        currentSearchParameter?.checkInDate = value
        currentSearchParameter?.id = UUID.randomUUID().toString()
        _searchParameters.value = currentSearchParameter
        Log.d("MainViewModel", "updateCheckInDate: ${_searchParameters.value}")

    }

    fun updateCheckOutDate(value: String) {
        val currentSearchParameter = searchParameters.value
        currentSearchParameter?.checkOutDate = value
        _searchParameters.value = currentSearchParameter
        Log.d("MainViewModel", "updateCheckOutDate: ${_searchParameters.value}")
    }

    fun saveSearchParameters(currentState: SearchParameter?) {
        viewModelScope.launch(Dispatchers.IO) {
            currentState?.let {
                it.id = UUID.randomUUID().toString()
                searchDao.insert(it)
            }
        }
    }

    fun formatDate(date: String): String {
        val dateParts = date.split("T")
        val units = dateParts[0].split("-")
        val year = units[0]
        val month = units[1]
        val day = units[2]
        return "$day/$month/$year"
    }
}

data class RowState(
    val imageRes: Int,
    val title: String,
)

data class RecentSearchState(
    val imageRes: Int,
    val title: String,
    val value: SearchParameter
)