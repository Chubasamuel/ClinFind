package com.chubasamuel.clinfind.data.remote

import com.chubasamuel.clinfind.data.local.Facility
import com.chubasamuel.clinfind.util.APIModels
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface Requests {

    @GET("/clinfind/app_update.json")
    fun getAppUpdate(): Call<APIModels.AppUpdateAPIModel?>?

    @GET("/clinfind/dev_update.json")
    fun getDevUpdate(): Call<APIModels.DevUpdateAPIModel?>?

    @GET("/clinfind/facilities.json")
    suspend fun getFacilities():Response<List<Facility>?>

}