package com.example.android.pocintenttest;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityMain =
            new IntentsTestRule<>(MainActivity.class, false, false);

    @Test
    public void givenLoadDisplay_WhenClickOther_ThenShootIntent() {
        initActivity();
        onView(withId(R.id.activity_main_other)).perform(click());
        intending(hasComponent(OtherActivity.class.getName())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void givenLoadDisplay_WhenClickOther_ThenShootIntentWithName() {
        initActivity();
        onView(withId(R.id.activity_main_other)).perform(click());
        Intent intentOther = new Intent(InstrumentationRegistry.getContext(), OtherActivity.class);
        intentOther.putExtra("name", "Alison");
        ActivityResult activityResultName = new ActivityResult(Activity.RESULT_OK, intentOther);
        intending(hasComponent(OtherActivity.class.getName())).respondWith(activityResultName);
    }

    @Test
    public void givenLoadDisplay_WhenClickBrowser_ThenShootIntentToChrome() {
        initActivity();
        onView(withId(R.id.activity_main_browser)).perform(click());
        intended(toPackage("com.android.chrome"));
    }

    @Test
    public void givenLoadDisplay_WhenClickBrowser_ThenShootUriGoogle() {
        initActivity();
        onView(withId(R.id.activity_main_browser)).perform(click());
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData("https://www.google.com.br"),
                toPackage("com.android.chrome")));
    }

    @Test
    public void givenLoadDisplay_WhenClickCall_ThenShootIntent() {
        initActivity();
        onView(withId(R.id.activity_main_call)).perform(click());
        intended(toPackage("com.android.server.telecom"));
    }

    @Test
    public void givenLoadDisplayCall_WhenShootIntent_ThenShowPhoneNumber() {
        grantPhonePermission();
        initActivity();
        onView(withId(R.id.activity_main_text_type_number)).perform(typeText("12345678"), closeSoftKeyboard());
        onView(withId(R.id.activity_main_call)).perform(click());
        intended(allOf(
                hasAction(Intent.ACTION_CALL),
                hasData("tel:12345678"),
                toPackage("com.android.server.telecom")));
    }

    @Test
    public void givenLoadDisplay_WhenClickPickNumber_ThenFillEditTextWithNumber() {
        initActivity();
        intending(hasComponent(hasShortClassName(".ContactActivity")))
                .respondWith(new ActivityResult(Activity.RESULT_OK,
                        ContactActivity.createResultData("12345678")));

        onView(withId(R.id.activity_main_call_pick_number)).perform(click());

        onView(withId(R.id.activity_main_text_type_number))
                .check(matches(withText("12345678")));
    }

    private void initActivity() {
        mActivityMain.launchActivity(new Intent());
    }

    private void grantPhonePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.CALL_PHONE");
        }
    }
}
