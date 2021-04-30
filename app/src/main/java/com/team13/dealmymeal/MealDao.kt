package com.team13.dealmymeal

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Query("SELECT * from meal")
    fun getAll(): Flow<List<Meal>>

    @Insert
    fun insertAll(vararg meals: Meal)

    @Delete
    fun delete(meal: Meal)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meal: Meal)

    @Query("DELETE FROM meal")
    suspend fun deleteAll()
}