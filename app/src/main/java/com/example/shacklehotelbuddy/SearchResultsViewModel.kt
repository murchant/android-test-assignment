package com.example.shacklehotelbuddy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shacklehotelbuddy.data.Respository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    private val repository: Respository
) : ViewModel() {

    private val _viewState = MutableLiveData<List<Respository.SearchResultWithDetails>>()
    val viewSate: LiveData<List<Respository.SearchResultWithDetails>>
        get() = _viewState

    fun bind(checkInDate: String, checkOutDate: String, adults: Int, children: Int) {
        viewModelScope.launch {
            val searchResults = repository.getSearchResults(checkInDate, checkOutDate, adults, children).filterNotNull()
            _viewState.postValue(searchResults)
        }
    }
}