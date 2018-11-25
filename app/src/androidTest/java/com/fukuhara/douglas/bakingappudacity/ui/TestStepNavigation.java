package com.fukuhara.douglas.bakingappudacity.ui;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.fukuhara.douglas.bakingappudacity.R;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by dofukuhara on 22/01/2018.
 */

@RunWith(AndroidJUnit4.class)
public class TestStepNavigation {

    @Rule
    public ActivityTestRule<RecipesListActivity> mActivityRule = new ActivityTestRule<>(
            RecipesListActivity.class);

    @Test
    public void iterateUntilLastStepFromFirstRecipe() throws InterruptedException {

        Thread.sleep(5000);

        // Click in the first Recipe from the Recycler View
        onView(ViewMatchers.withId(R.id.rv_recipes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Click in the first Step of the Steps List (we need to jump the first position because there
        // lays down the Ingredient List
        onView(withId(R.id.step_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        int i = 0;
        boolean finished = false;
        while (i < 500) {
            try {
                onView(withId(R.id.btn_next_step)).perform(click());
                i++;
            } catch (PerformException pf) {
                if (i > 0) {
                    // Success if found at least one Next Button than in the last screen this button is not available
                    finished = true;
                } else {
                    // False if the script did not detect a Next Button in the very first recipe step
                    finished = false;
                }
                break;
            }
        }

        Assert.assertTrue("Test Failed! Either reached the maxium execution threshold (500) " +
                "or did not find a Next Button in the First Step screen", finished);
    }
}
