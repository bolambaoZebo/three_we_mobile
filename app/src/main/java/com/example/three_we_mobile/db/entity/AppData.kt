package com.example.three_we_mobile.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize

@Entity(tableName = "appdata")
data class AppData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val isActive: Boolean? = null,
    val appVersion: Int? = null,
    val downloadLink: String? = null
)
