package com.nkdroid.tinderswipe;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by khoinguyen on 4/27/17.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class Login {


    public static final String WRONG_USERNAME = "22222faasc";
    public static final String RIGHT_USERNAME = "abcd@yahoo.com";
    public static final String PASSWORD = "123456";

    @Rule
    public ActivityTestRule<LogInActivity> mActivityRule = new ActivityTestRule<>(LogInActivity.class, true, false);

    @Test
    public void logInTestFail() {

        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.emailField))
                .perform(typeText(WRONG_USERNAME), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText(PASSWORD),closeSoftKeyboard());
        Espresso.closeSoftKeyboard();
        try {
            Thread.sleep(1000);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        onView(withId(R.id.loginButton)).perform(click());
    }

    @Test
    public void logInTestSuccess() {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.emailField))
                .perform(typeText(RIGHT_USERNAME), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText(PASSWORD),closeSoftKeyboard());
        Espresso.closeSoftKeyboard();
        try {
            Thread.sleep(1000);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        onView(withId(R.id.loginButton)).perform(click());
        try {
            Thread.sleep(3000);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
