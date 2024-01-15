package com.example.shacklehotelbuddy.data

import com.example.shacklehotelbuddy.data.network.CheckDate
import com.example.shacklehotelbuddy.data.network.PropertyDetailsResponse
import com.example.shacklehotelbuddy.data.network.SearchRequestBody
import com.example.shacklehotelbuddy.data.network.Service
import javax.inject.Inject

class Respository @Inject constructor(
    private val service: Service
) {

    suspend fun getSearchResults(
        checkInDate: String,
        checkOutDate: String,
        adults: Int,
        children: Int
    ): List<SearchResultWithDetails> {
        val searchResults = service.getSearchResults(
            SearchRequestBody(
                checkInDate = CheckDate(2024, 3, 1),
                checkOutDate = CheckDate(2024, 3, 6),
            )
        )
        return searchResults.properties.map {
            val details = getPropertyDetails(it.id)
            SearchResultWithDetails(
                hotelName = details.data.propertyInfo.summary.name,
                hotelCity = details.data.propertyInfo.location.address.city,
                hotelPrice = "£100",
                hotelRating = details.data.propertyInfo.summary.overview.propertyRating.rating.toString(),
                hotelImage = it.propertyImage.fallbackImage.url
            )
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