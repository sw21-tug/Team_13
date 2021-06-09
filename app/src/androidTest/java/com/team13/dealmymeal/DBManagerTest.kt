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
        mealDao.insert(meal)
        //val allMeals = mealDao.getAll().toList()
        val allMeals = mealDao.getAllTest()
        assertThat(allMeals.contains(meal)).isTrue()
        mealDao.deleteAll()
    }

    @Test
    fun deleteTestItems() = runBlocking {
        val meal = Meal("Spaghetti", listOf(0), 0f)
        val mealTest = Meal("asdfqwer1234", listOf(0), 0f)
        mealDao.insert(meal)
        mealDao.insert(mealTest)
        val allMeals = mealDao.getAllTest()
        assertEquals(2, allMeals.size)
        mealDao.deleteTestItems()
        val allMealsDelete = mealDao.getAllTest()
        assertEquals(1, allMealsDelete.size)
        assertFalse(allMealsDelete.contains(mealTest))
        assertTrue(allMealsDelete.contains(meal))
        mealDao.deleteAll()
    }

    @Test
    fun deleteById() = runBlocking {
        val meal = Meal("Spaghetti", listOf(0), 0f)
        mealDao.insert(meal)
        var allMeals = mealDao.getAllTest()
        val id = allMeals[0].id
        assertEquals(1, allMeals.size)
        mealDao.deleteById(12341234123412341L)
        allMeals = mealDao.getAllTest()
        assertEquals(1, allMeals.size)
        mealDao.deleteById(id)
        allMeals = mealDao.getAllTest()
        assertEquals(0, allMeals.size)
    }

    @Test
    fun updateMeal() = runBlocking {
        val meal = Meal("Spaghetti", listOf(0), 0f)
        mealDao.insert(meal)
        var allMeals = mealDao.getAllTest()
        val mealDb = allMeals[0]
        assertEquals(meal.rating, mealDb.rating)
        assertTrue(meal.categories == mealDb.categories)
        mealDao.updateMeal(mealDb.id, "Gemüsepfanne", 1f, listOf(Category.SPECIAL.category, Category.VEGGIE.category))
        val mealUpdated = mealDao.getAllTest()[0]
        assertEquals("Gemüsepfanne", mealUpdated.title)
        assertEquals(1f, mealUpdated.rating)
        assertTrue(mealUpdated.categories!!.contains(Category.SPECIAL.category))
    }

    @Test
    fun countTitle() = runBlocking {
        val meal = Meal("Spaghetti", listOf(0), 0f)
        val meal2 = Meal("Spaghetti", listOf(0), 0f)
        val meal3 = Meal("Toast", listOf(0), 0f)
        mealDao.insert(meal)
        mealDao.insert(meal2)
        mealDao.insert(meal3)
        val allMeals = mealDao.getAllTest()
        assertEquals(3, allMeals.size)
        assertEquals(2, mealDao.getCountTitle("Spaghetti"))
        assertEquals(1, mealDao.getCountTitle("Toast"))
    }

    @Test
    fun deleteWithTitle() = runBlocking {
        val meal = Meal("Spaghetti", listOf(0), 0f)
        val meal2 = Meal("Toast", listOf(0), 0f)
        mealDao.insert(meal)
        mealDao.insert(meal2)
        val allMeals = mealDao.getAllTest()
        assertEquals(2, allMeals.size)
        mealDao.deleteWithTitle(meal2.title!!)
        assertFalse(mealDao.getAllTest().contains(meal2))
        assertTrue(mealDao.getAllTest().contains(meal))
    }
}