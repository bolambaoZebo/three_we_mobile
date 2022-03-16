package com.example.three_we_mobile.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.three_we_mobile.db.dao.AppDao
import com.example.three_we_mobile.db.entity.AppData

@Database(
    entities = [
        AppData::class,
    ],
    version = 1,
    exportSchema = false
)

abstract class RoomDB: RoomDatabase() {

    abstract fun getDaoApp() : AppDao
//    abstract fun getDaoAds() : AdsDao
//    abstract fun getDaoApp() : AppDao

    companion object {
        private var DB_INSTANCE: RoomDB? = null

        fun getAppDBInstance(context: Context): RoomDB {
            if (DB_INSTANCE == null){
                DB_INSTANCE = Room.databaseBuilder(context.applicationContext,
                RoomDB::class.java,
                "WEISH_DB")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return DB_INSTANCE!!
        }
    }
}