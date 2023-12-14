package com.chubasamuel.clinfind.data.repository

import android.util.Log
import com.chubasamuel.clinfind.data.remote.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

fun <T> getData( apiReq: suspend () -> Resource<T>,
                        saveRes: suspend (T) -> Unit): Flow<Resource<T>> =
    callbackFlow {
        trySend(Resource.loading())
        CoroutineScope(Dispatchers.IO).launch {
        var retries = 0L
        var response = apiReq.invoke()
        while(retries<7&&response.status!=Resource.Status.SUCCESS){
            retries+=1
            delay(1000*retries)
            response = apiReq.invoke()
        }

        if (response.status == Resource.Status.SUCCESS) {
            Log.i("DCOR DEBUG","printing network res")
            Log.i("DCOR DEBUG","Network Data: ${response.data}")
            response.data?.let { trySend(Resource.success(it)); saveRes(it) }

        } else if (response.status == Resource.Status.ERROR) {
            trySend(Resource.error("Error fetching data over the internet."))
        }
            channel.close()
        }

        awaitClose{}
    }
fun <T> getDataV2(dbQuery:  () -> Flow<T>): Flow<Resource<T>> =
    flow {
        emit(Resource.loading())
        val source = dbQuery.invoke()
        source.collect{data->emit(Resource.success(data))}
    }