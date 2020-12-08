package com.smesonero.meepchallenge.ws.service

import arrow.core.Either
import com.smesonero.meepchallenge.base.ApiError
import com.smesonero.meepchallenge.ws.wsdata.WSResource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ResourceService{

    companion object {
        const val ENDPOINT = "https://apidev.meep.me/tripplan/api/v1/"
    }

    @GET("routers/lisboa/resources")
    suspend fun getLisboaResourcesList(@Query("lowerLeftLatLon") lowerLeftLatLon: String,
                 @Query("upperRightLatLon") upperRightLatLon: String)
                 : Either<ApiError, List<WSResource>>
}