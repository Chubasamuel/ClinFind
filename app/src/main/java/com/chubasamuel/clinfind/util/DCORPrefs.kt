package com.chubasamuel.clinfind.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class DCORPrefs(private val context:Context?){
    @Suppress("UNCHECKED_CAST")
    fun <T>check(keyS: String,defaultS: T):T{
        if(context==null)return defaultS
        val sPref:SharedPreferences= context.getSharedPreferences("dcor_data_dose",Context.MODE_PRIVATE)
        return when(defaultS){
            is String->sPref.getString(keyS,defaultS) as T
            is Int-> sPref.getInt(keyS,defaultS) as T
            is Long-> sPref.getLong(keyS,defaultS) as T
            is Boolean-> sPref.getBoolean(keyS,defaultS) as T
            else-> defaultS
        }
    }
    @SuppressLint("ApplySharedPref")
    fun save(sKey:String, sValue:Any, saveImmediately:Boolean=false){
        if(context==null)return
        val sharedPreferences:SharedPreferences = context.getSharedPreferences("dcor_data_dose", Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor = sharedPreferences.edit()
        when(sValue) {
            is String-> editor.putString(sKey, sValue)
            is Int->editor.putInt(sKey, sValue)
            is Long-> editor.putLong(sKey, sValue)
            is Boolean->editor.putBoolean(sKey, sValue)
        }
        if(saveImmediately){editor.commit()}
        else{ editor.apply()}
    }
    fun saveCurTabIndex(key:String,value:Int){
        save(key,value)
    }
    fun saveExportTypeByValue(b:Boolean){
        save("dcor_data_dose_value_export",b)
    }
    fun saveExportHeaderType(v:Int){
        save("dcor_data_dose_header_export",v)
    }
    fun getExportTypeByValue()=check("dcor_data_dose_value_export",true)
    fun getExportHeaderType()=check("dcor_data_dose_header_export",0)

    fun saveBoxAtStart(v:Boolean){
        save("dcor_data_dose_box_at_start",v)
    }
    fun getBoxAtStart() = check("dcor_data_dose_box_at_start",true)

    fun saveShowQLabel(v:Boolean){
        save("dcor_data_dose_show_q_label",v)
    }
    fun getShowQLabel() = check("dcor_data_dose_show_q_label",true)

    fun saveApiAuthToken(token:String){save("dcor_data_dose_api_auth_token",token)}
    fun getApiAuthToken() = check("dcor_data_dose_api_auth_token","")
}
