package com.team13.dealmymeal

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Meal::class], version = 1)
abstract class DBManager : RoomDatabase() {
    abstract fun mealDao(): MealDao
}