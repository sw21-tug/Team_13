package com.team13.dealmymeal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Query("SELECT * from meal")
    suspend fun getAll(): Flow<List<Meal>>

    @Insert
    suspend fun insertAll(vararg meals: Meal)

    @Query("DELETE from meal WHERE title='asdfqwer1234'")
    suspend fun deleteTestItems()

    @Delete
    suspend fun delete(meal: Meal)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meal: Meal)

    @Query("DELETE FROM meal")
    suspend fun deleteAll()
}