package com.medhat.todoapp.ui.addItemUi

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.medhat.todoapp.R
import com.medhat.todoapp.ui.listUi.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AddTodoItemActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        onView(withId(R.id.Todo_List_Add_Item_Floating)).perform(click())
    }

    @Test
    fun if_add_Activity_Viewed() {
        onView(withId(R.id.Add_New_Todo_Title_Label)).check(matches(isDisplayed()))
    }

    @Test
    fun check_add_new_Item() {
        onView(withId(R.id.Add_New_Todo_Title_EditText)).perform(ViewActions.typeText("Ahmed Medhat"))
        onView(withId(R.id.Add_New_Todo_Description_EditText)).perform(ViewActions.typeText("Ahmed Medhat Description"))
        onView(withId(R.id.Add_New_Todo_Time_Icon_Click)).perform(click())

        onView(withText("OK")).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.Add_New_Todo_Save_TextView)).perform(click())

        onView(withId(R.id.Todo_List_Add_Item_Floating)).check(matches(isDisplayed()))
    }

    @Test
    fun check_add_new_Item_Title_Error() {
        onView(withId(R.id.Add_New_Todo_Description_EditText)).perform(ViewActions.typeText("Ahmed Medhat Description"))
        onView(withId(R.id.Add_New_Todo_Time_Icon_Click)).perform(click())

        onView(withText("OK")).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.Add_New_Todo_Save_TextView)).perform(click())

        onView(withId(R.id.Add_New_Todo_Title_Error_TextView)).check(matches(isDisplayed()))
    }

    @Test
    fun check_add_new_Item_Description_Error() {
        onView(withId(R.id.Add_New_Todo_Title_EditText)).perform(ViewActions.typeText("Ahmed Medhat"))
        onView(withId(R.id.Add_New_Todo_Time_Icon_Click)).perform(click())

        onView(withText("OK")).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.Add_New_Todo_Save_TextView)).perform(click())

        onView(withId(R.id.Add_New_Todo_Description_Error_TextView)).check(matches(isDisplayed()))
    }

    @Test
    fun check_add_new_Item_Time_Error() {
        onView(withId(R.id.Add_New_Todo_Title_EditText)).perform(ViewActions.typeText("Ahmed Medhat"))
        onView(withId(R.id.Add_New_Todo_Description_EditText)).perform(ViewActions.typeText("Ahmed Medhat Description"))
        onView(withId(R.id.Add_New_Todo_Save_TextView)).perform(click())

        onView(withId(R.id.Add_New_Todo_Time_Error_TextView)).check(matches(isDisplayed()))
    }

}