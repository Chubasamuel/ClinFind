package com.chubasamuel.clinfind.ui.graphs

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.chubasamuel.clinfind.data.local.Facility
import com.chubasamuel.clinfind.data.local.FilterSearch
import com.chubasamuel.clinfind.data.remote.Resource
import com.chubasamuel.clinfind.ui.screens.AboutAppScreen
import com.chubasamuel.clinfind.ui.screens.HomeScreen
import com.chubasamuel.clinfind.viewmodel.AppViewModel
import kotlinx.coroutines.flow.combine

fun NavGraphBuilder.homeGraph(nav:NavHostController,snackHostState:SnackbarHostState){
    navigation(startDestination = Routes.home, Routes.start_home){
        composable(Routes.home){
            val vM: AppViewModel = hiltViewModel()
            val lCo= LocalLifecycleOwner.current
            val fAware=remember(vM.facilities,lCo){
                vM.facilities.flowWithLifecycle(lCo.lifecycle, Lifecycle.State.STARTED)
            }
            val snackAware=remember(vM.snack,lCo){
                vM.snack.flowWithLifecycle(lCo.lifecycle, Lifecycle.State.STARTED)
            }
            val snackText by snackAware.collectAsState(initial = null)
            val exF: Resource<List<Facility>> by fAware.collectAsState(initial = Resource.loading())

            val filtersAware = remember(vM.lgaS,vM.states,vM.specialties,vM.types,lCo) {
                combine(vM.lgaS,vM.states,vM.specialties,vM.types){
                        l,s,sp,t->FilterSearch(lgas=l,states=s, specialties = sp, types = t)
                }.flowWithLifecycle(lCo.lifecycle, Lifecycle.State.STARTED)
            }
            val filtersCollected by filtersAware.collectAsState(initial = FilterSearch(listOf(),listOf(),listOf(),listOf()))

            HomeScreen(snackHostState = snackHostState, snackText = snackText,
                //resetSnack ={vM.resetSnack()} ,
                facilities = exF, filterSearch = filtersCollected,
                onSearch = {s->vM.search(s)},
                goToAbout = { nav.navigate(Routes.about_app)}
                )

        }
        composable(Routes.about_app){
            AboutAppScreen()
        }
    }

}