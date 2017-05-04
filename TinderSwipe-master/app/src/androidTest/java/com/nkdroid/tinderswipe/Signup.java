package com.nkdroid.tinderswipe;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by khoinguyen on 4/27/17.
 */

public class Signup {

    public static final String signUpEmail = "testemail7@yahoo.com";
    public static final String signUpPass = "password";

    @Rule
    public ActivityTestRule<LogInActivity> mActivityRule = new ActivityTestRule<>(LogInActivity.class, true, false);

    @Test
    public void signUp () {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.signUpText)).perform(click());
        onView(withId(R.id.emailField)).perform(typeText(signUpEmail), closeSoftKeyboard());
        onView(withId(R.id.passwordField)).perform(typeText(signUpPass), closeSoftKeyboard());
        Espresso.closeSoftKeyboard();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onView(withId(R.id.signupButton)).perform(click());
        try {
            Thread.sleep(6000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //onView(withId(R.id.imageButton2)).perform(click());
    }

}

