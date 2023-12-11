package com.chubasamuel.clinfind.data.remote

class ApiSource (private val requests: Requests):BaseDataSource(){
suspend fun getFacilities() = getData{requests.getFacilities()}
}