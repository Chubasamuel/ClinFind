package com.chubasamuel.clinfind.data.repository

import android.util.Log
import com.chubasamuel.clinfind.data.remote.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
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
        val response = apiReq.invoke()
        if (response.status == Resource.Status.SUCCESS) {
            Log.i("DCOR DEBUG","printing network res")
            Log.i("DCOR DEBUG","Network Data: ${response.data}")
            response.data?.let { trySend(Resource.success(it)); saveRes(it) }

        } else if (response.status == Resource.Status.ERROR) {
            trySend(Resource.error(response.message?:"Unidentified error occurred while fetching data over the internet"))
        }}
        /*val source = dbQuery.invoke()
        source.collect{data->
            Log.i("DCOR DEBUG","printing res")
            Log.i("DCOR DEBUG","db res: $data")
            trySend(Resource.success(data))}*/
        awaitClose{}
    }
fun <T> getDataV2(dbQuery:  () -> Flow<T>): Flow<Resource<T>> =
    flow {
        emit(Resource.loading())
        val source = dbQuery.invoke()
        source.collect{data->emit(Resource.success(data))}
    }