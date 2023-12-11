package com.chubasamuel.clinfind.data.local

import androidx.sqlite.db.SimpleSQLiteQuery


//Can filter by [lga, state, specialty, type]
//Can search by [name, direction, owner, about]
data class Search(
    val param:String,
    val paramValue:String,
    val lga:String? = null,
    val state:String? = null,
    val specialty:String? = null,
    val type:String? = null
)

fun Search.getFilterQuery():SimpleSQLiteQuery{
    val s=this
    var query = "SELECT * FROM facility WHERE ${s.param} LIKE ? "
    val li = mutableListOf<String>()
    li.add(s.paramValue)

    if(s.lga!=null){query+=" AND lga=? "; li.add(s.lga)}
    if(s.state!=null){query+=" AND state=? "; li.add(s.state)}
    if(s.specialty!=null){query+=" AND specialty LIKE ? "; li.add(s.specialty)}
    if(s.type!=null){query+=" AND type=? "; li.add(s.type)}
    return SimpleSQLiteQuery(query, li.toTypedArray())
}

enum class FilterSearchBy(val label:String){
    lga("LGA"),
    state("State"),
    specialty("Specialty"),
    type("Type")
}
data class FilterSearch(
    val lgas:List<String>,
    val states:List<String>,
    val specialties:List<String>,
    val types:List<String>
)