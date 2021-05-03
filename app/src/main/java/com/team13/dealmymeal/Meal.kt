package com.team13.dealmymeal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal")
data class Meal(
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "stars") var stars: Int?,
    @ColumnInfo(name = "type") var type: Int?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

}