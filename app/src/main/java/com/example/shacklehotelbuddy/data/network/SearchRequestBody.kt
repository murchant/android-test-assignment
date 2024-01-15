package com.example.shacklehotelbuddy.data.network

import android.location.Address
import kotlinx.serialization.Serializable


@Serializable
data class PropertyDetailsResponse(
    val data: PropertyDetails
)

@Serializable
data class PropertyDetails(
    val propertyInfo: PropertyInfo,
)

@Serializable
data class PropertyInfo(
    val summary: Summary,
    val location: Location
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
    val overview: Overview
)

@Serializable
data class Overview(
    val propertyRating: PropertyRating,
)

@Serializable
data class PropertyRating(
    val rating: Int
)


@Serializable
data class SearchRequestResponse(
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
    val fallbackImage: FallbackImage
)

@Serializable
data class FallbackImage(
    val url: String
)

@Serializable
data class SearchRequestBody(
    val destination: Region = Region("6054439"),
    val checkInDate: CheckDate,
    val checkOutDate: CheckDate,
    val rooms: List<RoomConfiguration> = listOf(
        RoomConfiguration(
            adults = 1,
            children = listOf(Age(3)),
        )
    ),
    val limit: Int = 10
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