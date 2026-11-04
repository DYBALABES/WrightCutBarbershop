package com.wrightcut.barbershop.data.remote

import com.wrightcut.barbershop.data.model.WaitTimeResponse
import retrofit2.Response
import retrofit2.http.GET

interface BarberApiService {
    /**
     * Connects to online REST endpoint to fetch live walk-in wait times
     */
    @GET("api/v1/barbershop/wait-time")
    suspend fun getLiveWaitTime(): Response<WaitTimeResponse>
}