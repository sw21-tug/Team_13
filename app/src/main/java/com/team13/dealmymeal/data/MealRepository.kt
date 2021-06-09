package com.team13.dealmymeal.data

import androidx.annotation.WorkerThread
import com.team13.dealmymeal.core.Plan
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class MealRepository(private val mealDao: MealDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allMeals: Flow<List<Meal>> = mealDao.getAllMeals()
    val currentPlan: Flow<Plan> = mealDao.getCurrentPlan()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertMeal(meal: Meal) {
        mealDao.insertMeal(meal)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteMeal(meal: Meal) {
        mealDao.deleteMeal(meal)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteMealById(id: Long) {
        mealDao.deleteMealById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllMeals() {
        mealDao.deleteAllMeals()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getCountMeals(title: String): Int {
        return mealDao.getCountByMealTitle(title)
    }

    suspend fun updateMeal(meal: Meal) {
        mealDao.updateMeal(meal.id, meal.title!!, meal.rating!!, meal.categories!!)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteWithName(title: String) {
        mealDao.deleteWithMealTitle(title)
    }

}