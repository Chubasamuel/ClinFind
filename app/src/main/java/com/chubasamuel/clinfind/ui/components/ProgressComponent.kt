package com.chubasamuel.clinfind.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.chubasamuel.clinfind.data.remote.Resource
import com.chubasamuel.clinfind.ui.theme.CFT
import com.chubasamuel.clinfind.ui.theme.alertBgShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ErrorOccurred(text:String?=null,onDismiss:()->Unit={}){
    AlertDialog(onDismissRequest = { onDismiss() },
        content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Column(Modifier.background(color=CFT.colors.cardBg, shape= alertBgShape).padding(25.dp)){
                Text(
                    "Error occurred",
                    style = TextStyle(color = CFT.colors.error),
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(20.dp))
                Text(text?:"", color = CFT.colors.textColor)
            }}
                  },
        properties= DialogProperties(dismissOnBackPress = true,dismissOnClickOutside = true)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkSuccess(text:String?=null,onDismiss:()->Unit={}){
    AlertDialog(onDismissRequest = { onDismiss() },
        content = {
            Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
                Column(Modifier.background(color=CFT.colors.cardBg, shape= alertBgShape).padding(25.dp)){
            Text("Success", style = TextStyle(color = Color(0xFF55AA48)), fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(20.dp))
            Text(text?:"", color = CFT.colors.textColor)
                  }}},
        properties= DialogProperties(dismissOnBackPress = true,dismissOnClickOutside = true)
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndeterminateProgress(text:String?=null,onDismiss:()->Unit={}){
    AlertDialog(onDismissRequest = { onDismiss() },
        content={ Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ){
            Text(text?:"",
                Modifier.background(color=CFT.colors.cardBg, shape= alertBgShape)
                    .padding(35.dp),
                color = CFT.colors.textColor
            )
        }},
        properties= DialogProperties(dismissOnBackPress = false,dismissOnClickOutside = false)
    )
}

@Composable
fun WorkProgress(message:String, status:Resource.Status, resetWork:()->Unit, shouldWaitOnSuccess:Boolean=true){
    val density = LocalDensity.current
    AnimatedVisibility(visible = true,
        enter = slideInVertically { with(density){-80.dp.roundToPx()} },
        exit = slideOut(tween(250, easing= FastOutSlowInEasing)){IntOffset(-100,50) }
    ) {
        when (status) {
            Resource.Status.LOADING -> {
                IndeterminateProgress(message)
            }
            Resource.Status.ERROR -> {
                ErrorOccurred(message) {
                    resetWork()
                }
            }
            Resource.Status.SUCCESS -> {
                if(shouldWaitOnSuccess){
                    WorkSuccess(message){resetWork()}
                }
            }
        }
    }

}