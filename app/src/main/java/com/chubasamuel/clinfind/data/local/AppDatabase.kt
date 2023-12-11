package com.chubasamuel.clinfind.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities=[Facility::class],
    version=1, exportSchema = true,
    autoMigrations = []
)
@TypeConverters(Converters::class)
abstract class AppDatabase :RoomDatabase(){
    abstract fun getAppDao():AppDao

    companion object{
        @Volatile private var instance:AppDatabase?=null
        fun getInstance(ctx:Context):AppDatabase=
            instance?: synchronized(this){instance?:buildDatabase(ctx).also{instance=it}}
        private fun buildDatabase(appContext: Context)=
            Room.databaseBuilder(appContext,AppDatabase::class.java,"dcor_clinfind_db")
                .build()
    }
}