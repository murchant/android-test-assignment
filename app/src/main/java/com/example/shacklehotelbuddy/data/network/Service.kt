package com.example.shacklehotelbuddy.data.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.plugin
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class Service @Inject constructor() {

    suspend fun getSearchResults(searchRequestBody: SearchRequestBody): SearchRequestResponse {
        val client = createHttpClient()

        client.plugin(HttpSend).intercept { request ->
            val call = execute(request)
            Log.d("Service", "getSearchResults: ${request.body}")
            call
        }
        val response =
            client.post("https://hotels4.p.rapidapi.com/properties/v2/list") {
                header("X-RapidAPI-Key", "bb1ff95e2cmsha2b4b5f30beaa92p164cb9jsncaea5feaab79")
                header("X-RapidAPI-Host", "hotels4.p.rapidapi.com")
                contentType(ContentType.Application.Json)
                setBody(searchRequestBody)
//                setBody(searchRequestBody)
            }
        return response.body<SearchRequestResponse>()
    }

    private fun createHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    },
                    contentType = ContentType.Any
                )
            }
        }
    }

    suspend fun getPropertyDetails(id: String): PropertyDetailsResponse {
        val client = createHttpClient()
        val response =
            client.post("https://hotels4.p.rapidapi.com/properties/v2/detail") {
                header("X-RapidAPI-Key", "bb1ff95e2cmsha2b4b5f30beaa92p164cb9jsncaea5feaab79")
                header("X-RapidAPI-Host", "hotels4.p.rapidapi.com")
                contentType(ContentType.Application.Json)
                setBody(DetailsRequestBody(id))
            }
        return response.body<PropertyDetailsResponse>()
    }
}