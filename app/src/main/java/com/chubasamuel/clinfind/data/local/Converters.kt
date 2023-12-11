package com.chubasamuel.clinfind.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromLiOfString(value: List<String>?) = if (value == null) null else Gson().toJson(value)

    @TypeConverter
    fun toLiOfString(value:String?):List<String>?{
        value?:return null
        val type= TypeToken.getParameterized(List::class.java,String::class.java).type
        return Gson().fromJson(value,type)
    }
}