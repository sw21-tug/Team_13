package com.team13.dealmymeal


import android.widget.CheckBox
import android.widget.EditText
import org.junit.Test
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.*

import org.hamcrest.Matchers.not



import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {


    @Test
    fun form_buttonTest() {
        onView(withId(R.id.form_save)).perform(click())

    }

    @Test
    fun form_checkBoxes() {
        onView(withId(R.id.check_meat)).perform(click())
        onView(withId(R.id.check_veggie)).perform(click())
        onView(withId(R.id.check_special)).perform(click())


    }

    @Test
    fun form_TextInput() {
        onView(withId(R.id.form_edit)).perform(typeText("test"))
        onView(withId(R.id.form_edit)).check(matches(withText("test")))

    }
}