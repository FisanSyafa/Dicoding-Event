package com.example.mydicodingevents.data.remote.retrofit

import com.example.mydicodingevents.data.remote.response.EventResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("events?active=1")
    suspend fun getUpcomingEvents(
    ): Response<EventResponse>

    @GET("events?active=0")
    suspend fun getFinishedEvents(
    ): Response<EventResponse>

    @GET("events")
    suspend fun searchEvents(
        @Query("active") active: Int = -1,
        @Query("q") query: String
    ): Response<EventResponse>
}