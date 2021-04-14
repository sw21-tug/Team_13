package com.team13.dealmymeal

import android.graphics.Color
import android.service.autofill.Validators.not
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals


import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.team13.dealmymeal", appContext.packageName)
    }

    @Test
    fun test_meal_overview_list_item_edit_clicked() {
        onView(withText("2")).check(matches(isDisplayed()))
        onView(withText("2")).perform(click())
        //TODO should open edit popup
        onView(withText("2")).check(matches(hasTextColor(R.color.red)))
    }

    @Test
    fun test_meal_overview_list_item_edit_long_clicked() {
        onView(withText("2")).check(matches(isDisplayed()))
        onView(withText("2")).perform(click())
        //TODO activate selection for delete
        onView(withText("2")).check(matches(hasTextColor(R.color.teal_700)))
    }

}