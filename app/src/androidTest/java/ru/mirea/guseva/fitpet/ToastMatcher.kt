//package ru.mirea.guseva.fitpet
//
//import android.view.WindowManager
//import androidx.test.espresso.Root
//import org.hamcrest.Description
//import org.hamcrest.TypeSafeMatcher
//
//class ToastMatcher : TypeSafeMatcher<Root>() {
//    override fun describeTo(description: Description) {
//        description.appendText("is toast")
//    }
//
//    override fun matchesSafely(root: Root): Boolean {
//        val type = root.windowLayoutParams.get().type
//        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
//            val windowToken = root.decorView.windowToken
//            val appToken = root.decorView.applicationWindowToken
//            if (windowToken === appToken) {
//                // windowToken == appToken means this window isn't contained by any other windows.
//                // If it was a window for an activity, it would have TYPE_BASE_APPLICATION.
//                return true
//            }
//        }
//        return false
//    }
//}
