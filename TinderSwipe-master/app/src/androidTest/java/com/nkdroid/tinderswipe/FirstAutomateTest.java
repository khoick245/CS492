package com.nkdroid.tinderswipe;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.Toast;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


import com.nkdroid.tinderswipe.MainActivity;

/**
 * Created by khoinguyen on 4/26/17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FirstAutomateTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void logOutTest() {
        //onView(withId(R.id.imageButton2)).perform(click());
        onView(withId(R.id.logoutButton)).perform(click());
        try {
            Thread.sleep(3000);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
