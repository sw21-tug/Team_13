package com.team13.dealmymeal

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.team13.dealmymeal.data.DBManager
import com.team13.dealmymeal.data.Meal
import com.team13.dealmymeal.data.MealDao
import com.team13.dealmymeal.ui.overview.MealOverviewAdapter
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.*

import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DashBoardTest: TestCase() {

    private lateinit var db: DBManager
    private lateinit var mealDao: MealDao

    @get:Rule
    val activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    public override fun setUp() = runBlocking {
        // get context -- this is an instrumental test
        // context from the running application
        val context = ApplicationProvider.getApplicationContext<Context>()
        // init the db and dao variable
        db = Room.databaseBuilder(context, DBManager::class.java, "dmm.db").build()
        mealDao = db.mealDao()
    }

    @Test
    fun deleteButtonExists() {
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.delete)).check(matches(isDisplayed()))
    }

    /**
     * Tests if plan is displayed if a plan is in the database
     */
    @Test
    fun showPlan() = runBlocking  {
        onView(withId(R.id.navigation_dashboard)).perform(click())
        val plan = mealDao.getCurrentPlanTest()

        if (plan != null) {
            onView(withId(R.id.materialCardView)).check(matches(isDisplayed()))
        } else {
            assertTrue(true)
        }

        assertTrue(true)
    }


    @After
    fun cleanUp() = runBlocking {
        //mealDao.deleteTestItems()
    }


}