package com.chubasamuel.clinfind.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chubasamuel.clinfind.data.local.Facility
import com.chubasamuel.clinfind.data.local.Search
import com.chubasamuel.clinfind.data.remote.Resource
import com.chubasamuel.clinfind.data.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val repo: DataRepository) : ViewModel(){

    private val _facilities = MutableStateFlow<Resource<List<Facility>>>(Resource.loading())
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

    private var jobSearch:Job?=null

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
        viewModelScope.launch {
            repo.getFacilitiesLocal().collect{
                _facilities.value=it
            }
        }
        getFacilitiesFromApi()
    }
    private fun getFacilitiesFromApi(){
        viewModelScope.launch {
        repo.getFacilitiesFromApi().collect{
            _snack.value = it.message
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
}