package com.example.shacklehotelbuddy.data.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import javax.inject.Inject

class Service @Inject constructor() {

    suspend fun getSearchResults(searchRequestBody: SearchRequestBody): SearchRequestResponse {
        val response =
            createHttpClient().post("https://hotels4.p.rapidapi.com/properties/v2/list") {
                header("X-RapidAPI-Key", "bb1ff95e2cmsha2b4b5f30beaa92p164cb9jsncaea5feaab79")
                header("X-RapidAPI-Host", "hotels4.p.rapidapi.com")
                contentType(ContentType.Application.Json)
                setBody(searchRequestBody)
            }
        Log.d("Service", "getSearchResults: $response")
        return SearchRequestResponse(
            listOf(
                Property(
                    "123",
                    "ShangriLa Shard",
                    PropertyImage(FallbackImage("https://images.trvl-media.com/lodging/93000000/92930000/92928700/92928618/af462155.jpg?impolicy=resizecrop&rw=455&ra=fit"))
                ),
                Property(
                    "456",
                    "Ritz Calrton",
                    PropertyImage(FallbackImage("https://images.trvl-media.com/lodging/93000000/92930000/92928700/92928618/af462155.jpg?impolicy=resizecrop&rw=455&ra=fit"))
                ),
                Property(
                    "123",
                    "ShangriLa Shard",
                    PropertyImage(FallbackImage("https://images.trvl-media.com/lodging/93000000/92930000/92928700/92928618/af462155.jpg?impolicy=resizecrop&rw=455&ra=fit"))
                ),
                Property(
                    "456",
                    "Ritz Calrton",
                    PropertyImage(FallbackImage("https://images.trvl-media.com/lodging/93000000/92930000/92928700/92928618/af462155.jpg?impolicy=resizecrop&rw=455&ra=fit"))
                ),
                Property(
                    "123",
                    "ShangriLa Shard",
                    PropertyImage(FallbackImage("https://images.trvl-media.com/lodging/93000000/92930000/92928700/92928618/af462155.jpg?impolicy=resizecrop&rw=455&ra=fit"))
                ),
                Property(
                    "456",
                    "Ritz Calrton",
                    PropertyImage(FallbackImage("https://images.trvl-media.com/lodging/93000000/92930000/92928700/92928618/af462155.jpg?impolicy=resizecrop&rw=455&ra=fit"))
                )
            )
        )
    }

    private fun createHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    suspend fun getPropertyDetails(id: String): PropertyDetailsResponse {
        return PropertyDetailsResponse(
            PropertyDetails(
                PropertyInfo(
                    Summary(
                        "ShangriLa Shard",
                        Overview(
                            PropertyRating(5)
                        )
                    ),
                    Location(
                        AddressResponse("London")
                    )
                )
            )
        )
    }
}