package com.team13.dealmymeal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal")
data class Meal(
    @ColumnInfo(name = "title") val title: String?
) {
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
}