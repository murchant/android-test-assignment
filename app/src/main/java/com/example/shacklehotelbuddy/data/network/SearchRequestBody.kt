package com.example.shacklehotelbuddy.data.network

import android.location.Address
import kotlinx.serialization.Serializable


@Serializable
data class PropertyDetailsResponse(
    val data: PropertyDetails?
)

@Serializable
data class PropertyDetails(
    val propertyInfo: PropertyInfo,
)

@Serializable
data class PropertyInfo(
    val summary: Summary,
)

@Serializable
data class Location(
    val address: AddressResponse
)
@Serializable
data class AddressResponse(
    val city: String
)

@Serializable
data class Summary(
    val name: String,
    val overview: Overview,
    val location: Location
)

@Serializable
data class Overview(
    val propertyRating: PropertyRating,
)

@Serializable
data class PropertyRating(
    val rating: Float
)


@Serializable
data class SearchRequestResponse(
    val data : ResultData,
)

@Serializable
data class ResultData(
    val propertySearch: PropertySearch
)

@Serializable
data class PropertySearch(
    val properties: List<Property>
)

@Serializable
data class Property(
    val id: String,
    val name: String,
    val propertyImage: PropertyImage
)

@Serializable
data class PropertyImage(
    val image: Image
)

@Serializable
data class Image(
    val url: String
)

@Serializable
data class SearchRequestBody(
    val destination: Region,
    val checkInDate: CheckDate,
    val checkOutDate: CheckDate,
    val limit: Int,
    val rooms: List<RoomConfiguration>,
    val resultsSize: Int
)

@Serializable
data class DetailsRequestBody(
    val propertyId: String
)

@Serializable
data class Filters(
    val price: PriceFilter,
)

@Serializable
data class PriceFilter(
    val min: Int,
    val max: Int,
)

@Serializable
data class CheckDate(
    val year: Int,
    val month: Int,
    val day: Int,
)

@Serializable
data class RoomConfiguration(
    val adults: Int,
    val children: List<Age>,
)

@Serializable
data class Age(
    val age: Int,
)

@Serializable
data class Region(
    val regionId: String
)