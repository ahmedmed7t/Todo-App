package com.medhat.todoapp.ui.listUi

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.medhat.todoapp.R
import kotlinx.android.synthetic.main.activity_main.view.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.AdditionalMatchers.not

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun check_if_floating_Button_Appear() {
        onView(withId(R.id.Todo_List_Add_Item_Floating)).check(matches(isDisplayed()))
    }

    @Test
    fun check_if_empty_view_appear() {
        onView(withId(R.id.No_Items_Image)).check(matches(isDisplayed()))
    }

    @Test
    fun click_on_floating_button() {
        onView(withId(R.id.Todo_List_Add_Item_Floating)).perform(click())
        onView(withId(R.id.Add_New_Todo_Title_Label)).check(matches(isDisplayed()))
    }

}