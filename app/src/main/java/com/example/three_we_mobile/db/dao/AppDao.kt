package com.example.three_we_mobile.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.three_we_mobile.db.entity.AppData
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savaAllApp(app: AppData)

    @Query("DELETE FROM appdata")
    suspend fun deleteAllApp()

    @Query("SELECT * FROM appdata")
    fun getAllApp() : Flow<AppData>

}