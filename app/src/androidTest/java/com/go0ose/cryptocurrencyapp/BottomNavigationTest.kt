package com.go0ose.cryptocurrencyapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.go0ose.cryptocurrencyapp.presentation.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class BottomNavigationTest {

    @Test
    fun testBottomNavigation() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // Check that the initial item is selected
        onView(withId(R.id.mainScreenFragment))
            .check(matches(isSelected()))

        // Click on the second item
        onView(withId(R.id.settingScreenFragment))
            .perform(click())

        // Check that the second item is selected
        onView(withId(R.id.settingScreenFragment))
            .check(matches(isSelected()))
    }
}

