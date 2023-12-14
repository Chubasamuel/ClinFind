package com.chubasamuel.clinfind.data.remote

import com.chubasamuel.clinfind.data.local.Facility
import com.chubasamuel.clinfind.util.APIModels
import com.chubasamuel.clinfind.util.Links
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface Requests {

    @GET(Links.appUpdate)
    fun getAppUpdate(): Call<APIModels.AppUpdateAPIModel?>?

    @GET(Links.devUpdate)
    fun getDevUpdate(): Call<APIModels.DevUpdateAPIModel?>?

    @GET(Links.facilities)
    suspend fun getFacilities():Response<List<Facility>?>

}