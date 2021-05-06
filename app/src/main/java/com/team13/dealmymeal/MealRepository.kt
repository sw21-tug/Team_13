package com.team13.dealmymeal

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class MealRepository(private val mealDao: MealDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allMeals: Flow<List<Meal>> = mealDao.getAll()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(meal: Meal) {
        mealDao.insert(meal)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(meal: Meal) {
        mealDao.delete(meal)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        mealDao.deleteAll()
    }
}