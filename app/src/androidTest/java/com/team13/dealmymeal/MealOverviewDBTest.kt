package com.team13.dealmymeal

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class MealOverviewDBTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    @Test
    fun overview_clicked() {
        Espresso.onView(ViewMatchers.withId(R.id.navigation_overview)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.list)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun get_item_from_db() {
        DBManager.invoke(InstrumentationRegistry.getInstrumentation().getTargetContext()).mealDao().insertAll(Meal("Test"))
        Espresso.onView(ViewMatchers.withText("Test")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}