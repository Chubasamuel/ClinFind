package com.chubasamuel.clinfind.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chubasamuel.clinfind.data.local.Facility
import com.chubasamuel.clinfind.data.local.Search
import com.chubasamuel.clinfind.data.remote.Requests
import com.chubasamuel.clinfind.data.remote.Resource
import com.chubasamuel.clinfind.data.repository.DataRepository
import com.chubasamuel.clinfind.util.APIModels
import com.chubasamuel.clinfind.util.DCORPrefs
import com.chubasamuel.clinfind.util.UpdatesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val repo: DataRepository,
    private val dcorPrefs: DCORPrefs, private val requests: Requests
    ) : ViewModel(){

    private val _facilities = MutableStateFlow<Resource<List<Facility>?>>(Resource.loading())
    val facilities = _facilities.asStateFlow()
    private val _lgaS = MutableStateFlow<List<String>>(listOf())
    val lgaS = _lgaS.asStateFlow()
    private val _states = MutableStateFlow<List<String>>(listOf())
    val states = _states.asStateFlow()
    private val _specialties = MutableStateFlow<List<String>>(listOf())
    val specialties = _specialties.asStateFlow()
    private val _types = MutableStateFlow<List<String>>(listOf())
    val types = _types.asStateFlow()
    private val _snack = MutableStateFlow<String?>(null)
    val snack = _snack.asStateFlow()
    private val _devUpdate = MutableStateFlow<APIModels.APIUpdates?>(null)
    val devUpdate = _devUpdate.asStateFlow()
    private val _appUpdate = MutableStateFlow<APIModels.APIUpdates?>(null)
    val appUpdate = _appUpdate.asStateFlow()

    private var jobSearch:Job? = null
    private var jobFetch:Job? = null

    init{
        viewModelScope.launch {
            val lg=repo.getLGAs()

            lg.collect{
                if(it.status==Resource.Status.SUCCESS){
                    it.data?.let{d->_lgaS.value=it.data}}
        }}
        viewModelScope.launch {
            val ss=repo.getStates()
            ss.collect{if(it.status==Resource.Status.SUCCESS){
                it.data?.let{d->_states.value=it.data}}}
        }
        viewModelScope.launch {
            val sp=repo.getSpecialties()
            sp.collect{if(it.status==Resource.Status.SUCCESS){
                it.data?.let{d->_specialties.value=it.data}}}
        }
        viewModelScope.launch {
            val tt=repo.getTypes()
            tt.collect{if(it.status==Resource.Status.SUCCESS){
                it.data?.let{d->_types.value=it.data}}}
        }
        getFacilitiesLocal()
        getFacilitiesFromApi()
        mainCheckForUpdates()
    }
    private fun getFacilitiesLocal(){
        if(dcorPrefs.getAppDataFetchedForFirstTime()){
        viewModelScope.launch {
            repo.getFacilitiesLocal().collect{
                _facilities.value=it
            }
        }}
    }
    fun getFacilitiesFromApi(){
        jobFetch=viewModelScope.launch {
            val gotFirstTime = dcorPrefs.getAppDataFetchedForFirstTime()
        repo.getFacilitiesFromApi().collect{
            if(!gotFirstTime){
                _facilities.value=it
                if(it.status==Resource.Status.SUCCESS){
                    dcorPrefs.saveAppDataFetchedForFirstTime()
                    getFacilitiesLocal()
                    jobFetch?.cancel()
                }
            }else{
            _snack.value = it.message}
        }}
    }
    fun resetSnack(){_snack.value=null}

    fun search(s:Search){
        jobSearch?.cancel()
        jobSearch = viewModelScope.launch {
            repo.search(s).collect{
               if(it.status==Resource.Status.SUCCESS) _facilities.value=it
            }
        }
    }
    private suspend fun checkForApiUpdates(){
        UpdatesUtil.checkForUpdatesFromAPI(
            requests, dcorPrefs,
            onDevSuccess = {checkForDevUpdate()},
            onAppSuccess = {checkForAppUpdate()})
    }
    private fun checkForDevUpdate(){
        _devUpdate.value = UpdatesUtil.mainGetDevUpdate(dcorPrefs)
    }
    private fun checkForAppUpdate(){
        _appUpdate.value = UpdatesUtil.mainGetAppUpdate(dcorPrefs)
    }
    private fun mainCheckForUpdates(){
        CoroutineScope(Dispatchers.IO).launch {
            checkForDevUpdate(); checkForAppUpdate()
            checkForApiUpdates()
        }
    }
    fun saveDevUpdateInformed(update:APIModels.APIUpdates?){
        UpdatesUtil.saveLastDevUpdateInformed(dcorPrefs, update)
    }
    fun saveAppUpdateInformed(){UpdatesUtil.saveLastAppUpdateInformed(dcorPrefs)}
    fun resetDevUpdate(){_devUpdate.value=null}
    fun resetAppUpdate(){_appUpdate.value=null}
    fun launchPlayStore(context: Context){UpdatesUtil.launchPlayStore(context)}
}