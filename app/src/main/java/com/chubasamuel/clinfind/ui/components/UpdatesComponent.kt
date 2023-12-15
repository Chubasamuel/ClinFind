package com.chubasamuel.clinfind.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.window.DialogProperties
import com.chubasamuel.clinfind.util.APIModels.APIUpdates


@Composable
fun AppUpdateComponent(update: APIUpdates?, resetVal:()->Unit, saveLast:()->Unit, launchPlayStore:()->Unit, showSnackBar:(String)->Unit){
    val showAlert by remember(update){derivedStateOf { update!=null }}
    if(showAlert) {
        when(update) {
            is APIUpdates.ForceFulAlert->{
                ShowAPIAlert(
                    title = { Text(update.title) },
                    text = { Text(update.message) },
                    onConfirm = {
                        TextButton(onClick = { launchPlayStore() }) {
                            Text("Update")
                        }
                    },
                )}
            is APIUpdates.NormalAlert-> {
                ShowAPIAlert(title = { Text(update.title) },
                    text = { Text(update.message) },
                    onConfirm = {
                        TextButton(onClick = {
                            saveLast(); resetVal(); launchPlayStore()
                        }) {
                            Text("Update")
                        }
                    },
                    onDismiss = {
                        TextButton(onClick = { saveLast(); resetVal() }) {
                            Text("Cancel")
                        }
                    }
                )}
            is APIUpdates.SnackBar-> {
                showSnackBar(update.message)
            }
            else -> {}
        }
    }}

@Composable
fun DevUpdateComponent(update:APIUpdates?,resetVal: () -> Unit,saveLast:()->Unit){
    val showAlert by remember(update){derivedStateOf {update!=null}}
    if(showAlert){
        if(update is APIUpdates.NormalAlert){
            ShowAPIAlert(
                title = { Text(update.title) },
                text = { Text(update.message) },
                onConfirm = {
                    TextButton(onClick = { saveLast(); resetVal()}) {
                        Text("Okay")
                    }
                },
            )
        }
    }
}

@Composable
private fun ShowAPIAlert(title:@Composable ()->Unit, text:@Composable ()->Unit, onConfirm: @Composable ()->Unit,onDismiss: @Composable ()->Unit={}){
    AlertDialog(onDismissRequest = { },
        confirmButton = onConfirm,
        dismissButton = onDismiss,
        title = title,
        text= text,
        properties = DialogProperties(dismissOnBackPress = false,dismissOnClickOutside = false)
    )
}
