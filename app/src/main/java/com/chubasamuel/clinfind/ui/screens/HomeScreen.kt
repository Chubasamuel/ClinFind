package com.chubasamuel.clinfind.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chubasamuel.clinfind.data.local.Facility
import com.chubasamuel.clinfind.data.local.FilterSearch
import com.chubasamuel.clinfind.data.remote.Resource
import com.chubasamuel.clinfind.ui.components.FacilityComp
import com.chubasamuel.clinfind.ui.components.SearchComp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(snackHostState: SnackbarHostState,snackText:String?,/*resetSnack:()->Unit,*/ facilities: Resource<List<Facility>>, filterSearch: FilterSearch){
    LaunchedEffect(snackText){
    if(snackText!=null){
        CoroutineScope(Dispatchers.IO).launch {
            when(snackHostState.showSnackbar(message=snackText)){
                //SnackbarResult.ActionPerformed->{resetSnack()}
                else->{}
            }
        }
    }}


    when(facilities.status){
        Resource.Status.LOADING->{Text("Loading...")}
        Resource.Status.ERROR->{Text("Error")}
        Resource.Status.SUCCESS->{
            facilities.data?.let{
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 15.dp)){
                 SearchComp(onSearch = {}, filterSearch = filterSearch)
                LazyColumn(modifier=Modifier.padding(start=20.dp,end=20.dp, bottom = 20.dp, top=5.dp)){
                    items(it.size){
                        count->
                        FacilityComp(f = it[count])
                        Spacer(Modifier.height(15.dp))
                    }
                }
            }
        }
    }
}}