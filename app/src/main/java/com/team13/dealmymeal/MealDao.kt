package com.team13.dealmymeal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MealDao {
    @Query("SELECT * from meal")
    suspend fun getAll(): List<Meal>

    @Insert
    suspend fun insertAll(vararg meals: Meal)

    @Delete
    suspend fun delete(meal: Meal)
}