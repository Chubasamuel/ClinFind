package com.chubasamuel.clinfind.data.repository

import com.chubasamuel.clinfind.data.local.AppDao
import com.chubasamuel.clinfind.data.local.Search
import com.chubasamuel.clinfind.data.local.getFilterQuery
import com.chubasamuel.clinfind.data.remote.ApiSource
import javax.inject.Inject

class DataRepository @Inject constructor(private val dao:AppDao, private val api: ApiSource) {
    fun getFacilitiesFromApi() = getData(
        //dbQuery = {dao.getFacilities()},
        apiReq = {api.getFacilities()},
        saveRes = {data->data?.let { dao.clearFacilities(); dao.insertFacilities(data) }}
    )
    fun getFacilitiesLocal() = getDataV2(
        dbQuery = {dao.getFacilities()}
    )
    fun getFForLga(lga:String) = getDataV2 { dao.getFacilitiesForLga(lga) }
    fun getFForState(state:String) = getDataV2 { dao.getFacilitiesForLga(state) }
    fun getFForSpecialty(specialty:String) = getDataV2 { dao.getFacilitiesForLga(specialty) }
    fun search(s:Search) = getDataV2 { dao.search(s.getFilterQuery()) }

    fun getLGAs() =  getDataV2{dao.getUniqueLGAs()}
    fun getStates() =  getDataV2{dao.getUniqueStates()}
    fun getSpecialties() =  getDataV2{dao.getUniqueSpecialties()}
    fun getTypes() =  getDataV2{dao.getUniqueTypes()}

}