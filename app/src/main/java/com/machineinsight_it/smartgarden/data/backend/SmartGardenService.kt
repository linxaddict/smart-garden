package com.machineinsight_it.smartgarden.data.backend

import com.machineinsight_it.smartgarden.data.model.AuthData
import com.machineinsight_it.smartgarden.data.model.AuthResponseData
import com.machineinsight_it.smartgarden.data.model.RefreshTokenData
import com.machineinsight_it.smartgarden.data.model.RefreshTokenResponseData
import retrofit2.http.Body
import retrofit2.http.POST

interface SmartGardenService {
    @POST("auth/token")
    suspend fun authenticate(@Body authData: AuthData): AuthResponseData

    @POST("auth/token/refresh")
    suspend fun refreshToken(@Body refreshTokenData: RefreshTokenData): RefreshTokenResponseData
}