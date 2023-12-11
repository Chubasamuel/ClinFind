package com.chubasamuel.clinfind.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facility")
data class Facility(
    @PrimaryKey(autoGenerate = true) val id:Int?=null,
    val name:String,
    val type:String? = null,
    val specialty:String = "Unspecified",
    val owner:String? = null,
    val about:String? = null,
    val direction: String? = null,
    val lga:String,
    val state:String,
    val contact:List<String>? = null
)