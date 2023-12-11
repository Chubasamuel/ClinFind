package com.chubasamuel.clinfind.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFacilities(facilities:List<Facility>)

    @Query("SELECT * FROM facility  ORDER BY name")
    fun getFacilities(): Flow<List<Facility>>

    @Query("SELECT * FROM facility WHERE specialty=:specialty ORDER BY name")
    fun getFacilitiesForSpecialty(specialty:String): Flow<List<Facility>>

    @Query("SELECT * FROM facility WHERE lga=:lga ORDER BY name")
    fun getFacilitiesForLga(lga:String): Flow<List<Facility>>

    @Query("SELECT * FROM facility WHERE state=:state ORDER BY name")
    fun getFacilitiesForState(state:String): Flow<List<Facility>>

    @RawQuery(observedEntities = [Facility::class])
    fun searchWithFilter(query:SupportSQLiteQuery):Flow<List<Facility>>

    @Query("DELETE FROM facility")
    fun clearFacilities()

    @Query("SELECT DISTINCT lga FROM facility ORDER BY lga")
    fun getUniqueLGAs(): Flow<List<String>>
    @Query("SELECT DISTINCT state FROM facility ORDER BY state")
    fun getUniqueStates(): Flow<List<String>>
    @Query("SELECT DISTINCT specialty FROM facility ORDER BY specialty")
    fun getUniqueSpecialties(): Flow<List<String>>
    @Query("SELECT DISTINCT type FROM facility ORDER BY type")
    fun getUniqueTypes(): Flow<List<String>>
}


