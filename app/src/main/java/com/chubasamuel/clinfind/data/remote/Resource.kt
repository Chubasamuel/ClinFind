package com.chubasamuel.clinfind.data.remote

import androidx.compose.runtime.Composable

data class Resource<out T>(
    val status:Status,
    val data:T?,
    val message:String?
){
    enum class Status{
        LOADING, SUCCESS, ERROR
    }
    companion object{
        fun <T> success(data:T):Resource<T> = Resource(Status.SUCCESS,data,null)
        fun <T> error(message: String,data:T?=null) = Resource(Status.ERROR,data,message)
        fun <T> loading(data:T?=null,message: String?=null) = Resource(Status.LOADING,data,message)
    }
}
fun <T> Resource<T?>.handleResource(onLoading:(message:String?,T?)->Unit = {m,d->},
                                   onSuccess:(T?)->Unit,
                                   onError:(message:String?,T?)->Unit= {m,d->}
                                   ){
    when(this.status){
        Resource.Status.LOADING->onLoading(this.message, this.data)
        Resource.Status.SUCCESS->onSuccess(this.data)
        Resource.Status.ERROR->onError(this.message, this.data)
        }
}
@Composable
fun <T> Resource<T?>.HandleComposeResource(onLoading:@Composable (Resource<T?>)->Unit = {r->},
                                    onSuccess:@Composable (Resource<T?>)->Unit,
                                    onError:@Composable (Resource<T?>)->Unit= {r->}
){
    when(this.status){
        Resource.Status.LOADING->onLoading(this)
        Resource.Status.SUCCESS->onSuccess(this)
        Resource.Status.ERROR->onError(this)
    }
}
