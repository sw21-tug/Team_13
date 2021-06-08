package com.team13.dealmymeal.data

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromIntList(value : List<Int>) = Json.encodeToString(value)

    @TypeConverter
    fun toIntList(value: String) = Json.decodeFromString<List<Int>>(value)

    @TypeConverter
    fun fromLongList(value : List<Long>) = Json.encodeToString(value)

    @TypeConverter
    fun toLongList(value: String) = Json.decodeFromString<List<Long>>(value)
}