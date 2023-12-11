package com.chubasamuel.clinfind.data.remote

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