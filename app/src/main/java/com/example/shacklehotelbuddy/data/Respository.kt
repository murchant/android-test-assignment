package com.example.shacklehotelbuddy.data

import android.util.Log
import com.example.shacklehotelbuddy.data.network.Age
import com.example.shacklehotelbuddy.data.network.CheckDate
import com.example.shacklehotelbuddy.data.network.PropertyDetailsResponse
import com.example.shacklehotelbuddy.data.network.Region
import com.example.shacklehotelbuddy.data.network.RoomConfiguration
import com.example.shacklehotelbuddy.data.network.SearchRequestBody
import com.example.shacklehotelbuddy.data.network.Service
import javax.inject.Inject

class Respository @Inject constructor(
    private val service: Service
) {

    sealed class Result<out T> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Error(val exception: Exception) : Result<Nothing>()
        data class Empty(val message: String) : Result<Nothing>()
    }

    suspend fun getSearchResults(
        checkInDate: Triple<Int, Int, Int>,
        checkOutDate: Triple<Int, Int, Int>,
        adults: Int,
        children: Int
    ): Result<List<SearchResultWithDetails?>> {
        val childrenList = (0..children).map { Age(4) } // Set a Default age as it's not important for the task
        Log.d("Respository", "getSearchResults: $checkInDate, $checkOutDate, $adults, $children $childrenList")
        val searchResults = service.getSearchResults(
            SearchRequestBody(
                checkInDate = CheckDate(checkInDate.first, checkInDate.second, checkInDate.third),
                checkOutDate = CheckDate(checkOutDate.first, checkOutDate.second, checkOutDate.third),
                rooms = listOf(
                    RoomConfiguration(
                        adults = adults,
                        children = childrenList,
                    )
                ),
                destination = Region("6195474"),
                resultsSize = 5,
                limit = 5
            )
        )
        if(searchResults.data === null) {
            return Result.Empty("No results found")
        } else {
            val resultsWithData =  searchResults.data.propertySearch.properties.map {
                val details = getPropertyDetails(it.id)
                details.data?.let { propertyDetails ->
                    SearchResultWithDetails(
                        hotelName = propertyDetails.propertyInfo.summary.name,
                        hotelCity = propertyDetails.propertyInfo.summary.location.address.city,
                        hotelPrice = "£100",
                        hotelRating = propertyDetails.propertyInfo.summary.overview.propertyRating.rating.toString(),
                        hotelImage = it.propertyImage.image.url
                    )
                }
            }
            return Result.Success(resultsWithData)
        }
    }


    data class SearchResultWithDetails(
        val hotelName: String,
        val hotelCity: String,
        val hotelPrice: String,
        val hotelRating: String,
        val hotelImage: String
    )

    suspend fun getPropertyDetails(id: String): PropertyDetailsResponse {
        return service.getPropertyDetails(id)
    }
}