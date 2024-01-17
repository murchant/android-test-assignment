package com.example.shacklehotelbuddy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shacklehotelbuddy.data.Respository
import com.example.shacklehotelbuddy.data.SearchResultWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    private val repository: Respository
) : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>(ViewState.Loading)
    val viewSate: LiveData<ViewState>
        get() = _viewState

    private val coroutineUncaughtExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _viewState.postValue(ViewState.Error)
    }

    fun bind(checkInDate: String, checkOutDate: String, adults: Int, children: Int) {
        viewModelScope.launch(coroutineUncaughtExceptionHandler) {
            try {
                when(val searchResults = repository.getSearchResults(extractDate(checkInDate), extractDate(checkOutDate), adults, children)) {
                    is Respository.Result.Success -> {
                        _viewState.postValue(ViewState.Success(searchResults.data))
                    }
                    is Respository.Result.Error -> {
                        Log.d("SearchResultsViewModel", "bind: ${searchResults.exception}")
                        _viewState.postValue(ViewState.Loading)
                    }
                    is Respository.Result.Empty -> {
                        _viewState.postValue(ViewState.Empty)
                        Log.d("SearchResultsViewModel", "bind: ${searchResults.message}")
                    }
                }
            } catch (e: HttpRequestTimeoutException) {
                // I saw this crop up sometimes in testing
                _viewState.postValue(ViewState.Error)
            }
        }
    }

    private fun extractDate(date: String): Triple<Int, Int, Int> {
        val dateParts = date.split("T")[0].split("-")
        return Triple(dateParts[0].toInt(), dateParts[1].toInt(), dateParts[2].toInt())
    }

    sealed class ViewState {
        object Loading : ViewState()
        data class Success(val searchResults: List<SearchResultWithDetails?>) : ViewState()
        object Empty : ViewState()
        object Error : ViewState()
    }
}