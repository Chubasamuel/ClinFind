package com.chubasamuel.clinfind.data.remote

import android.util.Log
import retrofit2.Response

abstract class BaseDataSource {
    protected suspend fun <T> getData(call: suspend () -> Response<T>): Resource<T>  {
        return try {
            val res = call()
            Log.w("DCOR DEBUG","Api Response:: $res")
            if (res.isSuccessful) {
                val data = res.body()
                if (data != null) {
                    Resource.success(data)
                } else{Resource.error(message="Network call returned no data")}
            }else {
                Resource.error("Network call failed: ${res.message()}")
            }
        } catch (e: Exception) {
            Resource.error(e.message ?: "Unknown error occurred when making network request")
        }
    }
}