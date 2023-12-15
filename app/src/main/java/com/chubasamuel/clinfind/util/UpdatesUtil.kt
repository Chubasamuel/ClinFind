package com.chubasamuel.clinfind.util

import com.chubasamuel.clinfind.data.remote.Requests
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.chubasamuel.clinfind.util.APIModels.AppUpdateAPIModel
import com.chubasamuel.clinfind.util.APIModels.DevUpdateAPIModel
import com.chubasamuel.clinfind.util.APIModels.APIUpdates
import com.chubasamuel.clinfind.BuildConfig



object UpdatesUtil {
    suspend fun checkForUpdatesFromAPI(requests: Requests, dcorPrefs: DCORPrefs, onDevSuccess:()->Unit, onAppSuccess:()->Unit) {
        getDevUpdates(requests, dcorPrefs, onDevSuccess)
        getAppUpdates(requests, dcorPrefs, onAppSuccess)
    }
    private suspend fun getDevUpdates(requests: Requests, dcorPrefs: DCORPrefs, onSuccess:()->Unit, retry: Int = 3) {
        try {
            val res = requests.getDevUpdate()?.execute()
            if (res?.isSuccessful == true) {
                val data = res.body()
                data?.save(dcorPrefs)
                delay(2000)
                Log.w("DCOR DEBUG", "Got App Update -- $data")
                withContext(Dispatchers.Main){onSuccess()}
            } else {
                if (retry > 0) {
                    delay((4-retry)*1000L)
                    getDevUpdates(requests, dcorPrefs, onSuccess,retry - 1)
                }
            }
        } catch (e: Exception) {
            Log.w("DCOR DEBUG", "Error getting dev update - ${e.message}")
            e.printStackTrace()
        }
    }

    private suspend fun getAppUpdates(requests: Requests, dcorPrefs: DCORPrefs, onSuccess:()->Unit, retry: Int = 3) {
        try {
            val res = requests.getAppUpdate()?.execute()
            if (res?.isSuccessful == true) {
                val data = res.body()
                data?.save(dcorPrefs)
                Log.w("DCOR DEBUG", "Got Dev Update -- $data")
                delay(2000)
                withContext(Dispatchers.Main){onSuccess()}
            } else {
                if (retry > 0) {
                    delay((4-retry)*1000L)
                    getAppUpdates(requests, dcorPrefs,onSuccess, retry - 1)
                }
            }
        } catch (e: Exception) {
            Log.w("DCOR DEBUG", "Error getting app dev - ${e.message}")
            e.printStackTrace()
        }
    }

    private fun DevUpdateAPIModel.save(dcorPrefs: DCORPrefs) {
        dcorPrefs.save( SCONSTS.dev_info_title, this.dev_info_title)
        dcorPrefs.save( SCONSTS.dev_info_version, this.dev_info_version)
        dcorPrefs.save( SCONSTS.dev_start_date, this.dev_start_date)
        dcorPrefs.save( SCONSTS.dev_end_date, this.dev_end_date)
        dcorPrefs.save( SCONSTS.dev_remind_freq, this.dev_remind_freq)
        dcorPrefs.save( SCONSTS.dev_inform_once, this.dev_inform_once)
        dcorPrefs.save( SCONSTS.dev_info, this.dev_info)
        dcorPrefs.save( SCONSTS.dev_btn_okay_text, this.dev_btn_okay_text)
    }

    private fun AppUpdateAPIModel.save(dcorPrefs: DCORPrefs) {
        dcorPrefs.save(
            SCONSTS.update_app_version_code,
            this.update_app_version_code
        )
        dcorPrefs.save(
            SCONSTS.update_app_version_name,
            this.update_app_version_name
        )
        dcorPrefs.save( SCONSTS.update_type, this.update_type)
        dcorPrefs.save( SCONSTS.update_title, this.update_title)
        dcorPrefs.save( SCONSTS.update_info, this.update_info)
        dcorPrefs.save( SCONSTS.update_view_type, this.update_view_type)
        dcorPrefs.save( SCONSTS.update_enforcing, this.update_enforcing)
        dcorPrefs.save(
            SCONSTS.update_enforcing_minversion,
            this.update_enforcing_minversion
        )
        dcorPrefs.save( SCONSTS.update_severity, this.update_severity)
    }

    private fun isAppOutdated(dcorPrefs: DCORPrefs, vCode:Long): Boolean {
        return try {
            dcorPrefs.check(
                SCONSTS.update_app_version_code,
                0
            ) > vCode
        } catch (e: Exception) {
            false
        }
    }

    private fun isAppOutdatedAndEnforcingMinimumUpdate(
        dcorPrefs: DCORPrefs, vCode: Long
    ): Boolean {
        return try {
            dcorPrefs.check(
                SCONSTS.update_enforcing_minversion,
                0
            ) > vCode &&
                    isAppOutdated(dcorPrefs, vCode) && dcorPrefs.check(

                SCONSTS.update_enforcing,
                false
            )
        } catch (e: Exception) {
            false
        }
    }
    private fun isPastTime(dcorPrefs: DCORPrefs): Boolean {
        val tM = dcorPrefs.check( SCONSTS.update_last_informed, 0L)
        val date = Date()
        val diff: Long = date.time - tM
        return diff / (1000 * 60 * 60) >= 24
    }
    private fun isDevUpdateWithinTime(model: DevUpdateAPIModel): Boolean {
        val dateSDF = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        return try {
            val startTimestamp = dateSDF.parse(model.dev_start_date)?.time ?: 0L
            val stopTimestamp = dateSDF.parse(model.dev_end_date)?.time ?: 0L
            val currentTimestamp = Date().time
            currentTimestamp in startTimestamp..stopTimestamp
        } catch (e: Exception) {
            false
        }
    }

    private fun isRightToInformAboutDevUpdate(
        dcorPrefs: DCORPrefs,
        model: DevUpdateAPIModel
    ): Boolean {
        if (model.dev_inform_once && dcorPrefs.check(
                SCONSTS.dev_update_informed_version,
                0
            ) == model.dev_info_version
        ) {
            return false
        }
        if (model.dev_inform_once && dcorPrefs.check(

                SCONSTS.dev_update_informed_version,
                0
            ) != model.dev_info_version
        ) {
            return isDevUpdateWithinTime(model)
        }

        if ((!model.dev_inform_once) && isDevUpdateWithinTime(model)) {

            if(dcorPrefs.check(SCONSTS.dev_update_informed_version, 0) != model.dev_info_version){
                return true
            }
            val lastInformed = dcorPrefs.check(

                SCONSTS.dev_update_last_informed_timestamp,
                0L
            )
            val currentTimestamp = Date().time
            val currDiff = (currentTimestamp - lastInformed) / (1000 * 60 * 60 * 24)

            return currDiff == (currentTimestamp/ (1000 * 60 * 60 * 24)) || currDiff >= model.dev_remind_freq
        }
        return false
    }

    private fun getNormalAppUpdate(dcorPrefs: DCORPrefs ):APIUpdates {
        return if (dcorPrefs.check( SCONSTS.update_view_type, "")
                .lowercase(Locale.getDefault()).trim { it <= ' ' } == "snackbar"
        ) {
            val updateVersionName =    dcorPrefs.check(
                SCONSTS.update_app_version_name,
                ""
            )
            val msg=String.format(
                Locale.ENGLISH, "DataDose v%s build %d is available", updateVersionName,
                dcorPrefs.check(

                    SCONSTS.update_app_version_code,
                    0
                )
            )
            APIUpdates.SnackBar(msg)
        } else {
            getAppUpdate(dcorPrefs)
        }
    }
    private fun getDevUpdate(dcorPrefs: DCORPrefs):APIUpdates? {
        val model = getDevUpdateFromPref(dcorPrefs)
        return if(isRightToInformAboutDevUpdate(dcorPrefs, model)) {
            APIUpdates.NormalAlert(model.dev_info_title,model.dev_info,model.dev_info_version)
        }
        else{null}
    }
    private fun getAppUpdate(dcorPrefs: DCORPrefs):APIUpdates {
        val title= dcorPrefs.check(
            SCONSTS.update_title,
            "App Update Is Available"
        )
        val updateMsg = getUpdateMsg(dcorPrefs)
        return APIUpdates.NormalAlert(title,updateMsg)
    }

    private fun getForcefulAlertUpdate(dcorPrefs: DCORPrefs):APIUpdates {
        val title = dcorPrefs.check(
            SCONSTS.update_title,
            "Compulsory App Update"
        )
        val updateMsg = getUpdateMsg(dcorPrefs)
        return  APIUpdates.ForceFulAlert(title,updateMsg)
    }

    private fun getUpdateMsg(dcorPrefs: DCORPrefs): String {
        var res = ""
        res += "DataDose version " + dcorPrefs.check(

            SCONSTS.update_app_version_name,
            ""
        )
        res += """ build ${
            dcorPrefs.check(

                SCONSTS.update_app_version_code,
                0
            )
        } is now available

"""
        res += """	${dcorPrefs.check( SCONSTS.update_info, "")}

"""
        return res
    }

    private fun getDevUpdateFromPref(dcorPrefs: DCORPrefs): DevUpdateAPIModel {
        return DevUpdateAPIModel(
            dcorPrefs.check(SCONSTS.dev_info_title, ""),
            dcorPrefs.check(SCONSTS.dev_info_version, -1),
            dcorPrefs.check(SCONSTS.dev_start_date, ""),
            dcorPrefs.check(SCONSTS.dev_end_date, ""),
            dcorPrefs.check(SCONSTS.dev_remind_freq, -1),
            dcorPrefs.check(SCONSTS.dev_inform_once, true),
            dcorPrefs.check(SCONSTS.dev_info, ""),
            dcorPrefs.check(SCONSTS.dev_btn_okay_text, "Okay Thanks")
        )
    }

    private fun getAppUpdateHelper(dcorPrefs: DCORPrefs, vCode: Long): APIUpdates? {
        return try {
            if (isAppOutdatedAndEnforcingMinimumUpdate(dcorPrefs, vCode)) {
                getForcefulAlertUpdate(dcorPrefs)
            } else if (isAppOutdated(dcorPrefs, vCode) && isPastTime(dcorPrefs)) {
                getNormalAppUpdate(dcorPrefs)
            }else{
                Log.w("DCOR DEBUG", "Failed to get appUpdate")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    fun saveLastDevUpdateInformed(dcorPrefs: DCORPrefs, update: APIUpdates?){
        dcorPrefs.save(
            SCONSTS.dev_update_last_informed_timestamp,
            Date().time
        )
        update?.let{dcorPrefs.save(SCONSTS.dev_update_informed_version,it.update_version)}
    }
    fun saveLastAppUpdateInformed(dcorPrefs: DCORPrefs) {
        dcorPrefs.save( SCONSTS.update_last_informed, Date().time)
    }
    fun launchPlayStore(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
        intent.data =
            Uri.parse("http://play.google.com/store/apps/details?id=com.chubasamuel.clinfind")
        val chooser = Intent.createChooser(intent, "launch Play store")
        context.startActivity(chooser)
    }
    fun getAppVCode() = BuildConfig.VERSION_CODE.toLong()

    fun getAppVName() = BuildConfig.VERSION_NAME
    fun mainGetAppUpdate(dcorPrefs: DCORPrefs):APIUpdates? = getAppUpdateHelper(dcorPrefs, getAppVCode())
    fun mainGetDevUpdate(dcorPrefs: DCORPrefs):APIUpdates? = getDevUpdate(dcorPrefs)
}