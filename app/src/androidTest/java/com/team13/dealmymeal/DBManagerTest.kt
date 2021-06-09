package com.team13.dealmymeal

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.team13.dealmymeal.data.Category
import com.team13.dealmymeal.data.DBManager
import com.team13.dealmymeal.data.Meal
import com.team13.dealmymeal.data.MealDao
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class) // Annotate with @RunWith
class DBManagerTest: TestCase() {
    private lateinit var db: DBManager
    private lateinit var mealDao: MealDao

    // the setUp function is called before the first test is run, it builds the database,
    // @Before
    @Before
    public override fun setUp() {
        // get context -- this is an instrumental test
        // context from the running application
        val context = ApplicationProvider.getApplicationContext<Context>()
        // init the db and dao variable
        db = Room.inMemoryDatabaseBuilder(context, DBManager::class.java).build()
        mealDao = db.mealDao()
    }

    // The function closeDb is called after the last test, annotated with @After
    @After
    fun closeDb() {
        db.close()
    }

    // this is a test function annotated with @Test
    // here we are first adding an item to the db and then checking if that item
    // is present in the db -- if the item is present then our test cases pass
    @Test
    fun checkInsert() = runBlocking {
        val meal = Meal("Spaghetti", listOf(0), 0f)
        mealDao.insertMeal(meal)
        //val allMeals = mealDao.getAll().toList()
        val allMeals = mealDao.getAllMealsTest()
        assertThat(allMeals.contains(meal)).isTrue()
        mealDao.deleteAllMeals()
    }

    @Test
    fun deleteTestItems() = runBlocking {
        val meal = Meal("Spaghetti", listOf(0), 0f)
        val mealTest = Meal("asdfqwer1234", listOf(0), 0f)
        mealDao.insertMeal(meal)
        mealDao.insertMeal(mealTest)
        val allMeals = mealDao.getAllMealsTest()
        assertEquals(2, allMeals.size)
        mealDao.deleteTestItems()
        val allMealsDelete = mealDao.getAllMealsTest()
        assertEquals(1, allMealsDelete.size)
        assertFalse(allMealsDelete.contains(mealTest))
        assertTrue(allMealsDelete.contains(meal))
        mealDao.deleteAllMeals()
    }

    @Test
    fun deleteById() = runBlocking {
        val meal = Meal("Spaghetti", listOf(0), 0f)
        mealDao.insertMeal(meal)
        var allMeals = mealDao.getAllMealsTest()
        val id = allMeals[0].id
        assertEquals(1, allMeals.size)
        mealDao.deleteMealById(12341234123412341L)
        allMeals = mealDao.getAllMealsTest()
        assertEquals(1, allMeals.size)
        mealDao.deleteMealById(id)
        allMeals = mealDao.getAllMealsTest()
        assertEquals(0, allMeals.size)
    }

    @Test
    fun updateMeal() = runBlocking {
        val meal = Meal("Spaghetti", listOf(0), 0f)
        mealDao.insertMeal(meal)
        var allMeals = mealDao.getAllMealsTest()
        val mealDb = allMeals[0]
        assertEquals(meal.rating, mealDb.rating)
        assertTrue(meal.categories == mealDb.categories)
        mealDao.updateMeal(mealDb.id, "Gemüsepfanne", 1f, listOf(Category.SPECIAL.category, Category.VEGGIE.category))
        val mealUpdated = mealDao.getAllMealsTest()[0]
        assertEquals("Gemüsepfanne", mealUpdated.title)
        assertEquals(1f, mealUpdated.rating)
        assertTrue(mealUpdated.categories!!.contains(Category.SPECIAL.category))
    }

    @Test
    fun countTitle() = runBlocking {
        val meal = Meal("Spaghetti", listOf(0), 0f)
        val meal2 = Meal("Spaghetti", listOf(0), 0f)
        val meal3 = Meal("Toast", listOf(0), 0f)
        mealDao.insertMeal(meal)
        mealDao.insertMeal(meal2)
        mealDao.insertMeal(meal3)
        val allMeals = mealDao.getAllMealsTest()
        assertEquals(3, allMeals.size)
        assertEquals(2, mealDao.getCountByMealTitle("Spaghetti"))
        assertEquals(1, mealDao.getCountByMealTitle("Toast"))
    }

    @Test
    fun deleteWithTitle() = runBlocking {
        val meal = Meal("Spaghetti", listOf(0), 0f)
        val meal2 = Meal("Toast", listOf(0), 0f)
        mealDao.insertMeal(meal)
        mealDao.insertMeal(meal2)
        val allMeals = mealDao.getAllMealsTest()
        assertEquals(2, allMeals.size)
        mealDao.deleteWithMealTitle(meal2.title!!)
        assertFalse(mealDao.getAllMealsTest().contains(meal2))
        assertTrue(mealDao.getAllMealsTest().contains(meal))
    }
}