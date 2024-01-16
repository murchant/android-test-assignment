package com.example.shacklehotelbuddy.data

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

    suspend fun getSearchResults(
        checkInDate: String,
        checkOutDate: String,
        adults: Int,
        children: Int
    ): List<SearchResultWithDetails?> {
        val searchResults = service.getSearchResults(
            SearchRequestBody(
                checkInDate = CheckDate(2024, 3, 1),
                checkOutDate = CheckDate(2024, 3, 6),
                rooms = listOf(
                    RoomConfiguration(
                        adults = 1,
                        children = listOf(Age(3)),
                    )
                ),
                destination = Region("6054439"),
                resultsSize = 10,
                limit = 10
            )
        )
        return searchResults.data.propertySearch.properties.map {
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