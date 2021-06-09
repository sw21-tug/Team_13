package com.team13.dealmymeal.data

import androidx.room.*
import com.team13.dealmymeal.core.Plan
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Query("SELECT * from meal")
    fun getAllMeals(): Flow<List<Meal>>

    @Insert
    fun insertAllMeals(vararg meals: Meal)

    //This is a test function - DO NOT USE (except in tests)
    @Query("DELETE from meal WHERE title='asdfqwer1234'")
    fun deleteTestItems()

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("DELETE from meal WHERE id= :id")
    suspend fun deleteMealById(id: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMeal(meal: Meal)

    @Query("DELETE FROM meal")
    suspend fun deleteAllMeals()

    @Query("UPDATE meal SET title=:title, rating=:rating, categories=:categories WHERE id=:id")
    suspend fun updateMeal(id: Long, title: String, rating: Float, categories: List<Int?>)

    //This is a test function - DO NOT USE (except in tests)
    @Query("SELECT * from meal")
    suspend fun getAllMealsTest(): List<Meal>

    @Query("SELECT COUNT(id) FROM meal WHERE title=:title")
    suspend fun getCountByMealTitle(title: String): Int

    @Query("DELETE from meal WHERE title=:title")
    suspend fun deleteWithMealTitle(title:String)

    @Query("SELECT * FROM 'plan' WHERE created_time = (SELECT MAX(created_time) FROM 'plan')")
    fun getCurrentPlan(): Flow<Plan>

    @Query("DELETE FROM 'plan' WHERE id=:id")
    suspend fun deletePlanWithId(id: Long)

    @Query("DELETE FROM 'plan'")
    suspend fun deleteAllPlans()

    @Query("INSERT INTO 'plan' (period, meals_per_day, meals) values (:period, :mealsPerDay, :meals)")
    suspend fun insertPlan(period: Int, mealsPerDay:Int, meals: String)

    @Query("SELECT * FROM 'plan' WHERE created_time = (SELECT MAX(created_time) FROM 'plan')")
    fun getCurrentPlanTest(): Plan?
}