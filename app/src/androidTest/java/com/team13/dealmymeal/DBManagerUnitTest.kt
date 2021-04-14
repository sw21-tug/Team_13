package com.team13.dealmymeal

import android.content.Context
import androidx.room.Room
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DBManagerUnitTest {
    private lateinit var mealDao: MealDao
    private lateinit var db: DBManager

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, DBManager::class.java).build()
        mealDao = db.mealDao()
    }


    @Test
    fun saveStoreDBManagerTest() {
        val meal = Meal(1, "Spaghetti")
        mealDao.insertAll(meal)

        val meal_db = mealDao.getAll()

        assertEquals("Save and Store data DBManager", meal_db[0].title, "Spaghetti")
    }
}