package com.wrightcut.barbershop.data.model

import com.google.gson.annotations.SerializedName

data class WaitTimeResponse(
    @SerializedName("shopStatus") val shopStatus: String,
    @SerializedName("estimatedMinutes") val estimatedMinutes: Int,
    @SerializedName("barbersActive") val barbersActive: Int
)