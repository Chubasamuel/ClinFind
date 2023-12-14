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

    val paramValues=s.paramValue.split(Regex("\\s+"))
    li.add("%"+paramValues[0]+"%")
    if(paramValues.size>1){
    for(i in 1 until paramValues.size){
        query+=" AND ${s.param} LIKE ? "
        li.add("%${paramValues[i]}%")
    }}

    query+=" OR ${s.param} LIKE ? "
    li.add("%"+s.paramValue+"%")

    if(s.lga!=null){query+=" AND lga=? "; li.add(s.lga)}
    if(s.state!=null){query+=" AND state=? "; li.add(s.state)}
    if(s.specialty!=null){query+=" AND specialty LIKE ? "; li.add("%"+s.specialty+"%")}
    if(s.type!=null){query+=" AND type=? "; li.add(s.type)}
    query+=" ORDER BY name"
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