package com.chubasamuel.clinfind.util

object APIModels {
    data class AppUpdateAPIModel(
        val  update_app_version_code: Int,
        val  update_app_version_name: String,
        val  update_type: String,
        val  update_title: String,
        val  update_info: String,
        val  update_view_type: String,
        val  update_enforcing: Boolean,
        val  update_enforcing_minversion: Int,
        val  update_severity: String
    )

    data class DevUpdateAPIModel(
        val  dev_info_title: String,
        val  dev_info_version: Int,
        val  dev_start_date: String,
        val  dev_end_date: String,
        val  dev_remind_freq: Int,
        val  dev_inform_once: Boolean,
        val  dev_info: String,
        val  dev_btn_okay_text: String
    )

    sealed class APIUpdates(val update_version:Int){
        data class ForceFulAlert(val title:String,val message:String,val version:Int=0):APIUpdates(version)
        data class NormalAlert(val title:String,val message:String,val version:Int=0):APIUpdates(version)
        data class SnackBar(val message:String,val version:Int=0):APIUpdates(version)
    }
}

