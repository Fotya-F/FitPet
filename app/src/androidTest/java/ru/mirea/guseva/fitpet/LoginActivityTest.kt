package ru.mirea.guseva.fitpet

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)

    private lateinit var auth: FirebaseAuth

    @Before
    fun setUp() {
        auth = FirebaseAuth.getInstance()
        auth.signOut() // Ensure no user is logged in before each test
    }

    @Test
    fun testLoginSuccess() {
        // Launch LoginActivity
        ActivityScenario.launch(LoginActivity::class.java)

        // Enter email and password
        onView(withId(R.id.email)).perform(typeText("gusevasv29@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText("administrator"), closeSoftKeyboard())

        // Click login button
        onView(withId(R.id.login_button)).perform(click())

        // Check if MainActivity is displayed by verifying unique element
        onView(withId(R.id.main_activity_unique_element)).check(matches(isDisplayed()))
    }

    @Test
    fun testLoginFailure() {
        // Launch LoginActivity
        ActivityScenario.launch(LoginActivity::class.java)

        // Enter invalid email and password
        onView(withId(R.id.email)).perform(typeText("wronguser@example.com"), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText("wrongpassword"), closeSoftKeyboard())

        // Click login button
        onView(withId(R.id.login_button)).perform(click())

        // Check if error message is displayed
        onView(withText("Authentication failed.")).inRoot(ToastMatcher()).check(matches(isDisplayed()))
    }
}
