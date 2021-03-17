package com.hieunguyen.snacktruck;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatButton;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.hieunguyen.snacktruck.bean.FoodType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


/**
 * UI Testing class for main activity that automate tests for basic user cases of submit order,
 * filter menu, and add new menu item.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void submitOrderTest() {
        final String[] selectedItemNames = {""};

        // Get the name of the first order in the menu item
        onView(withId(R.id.rv_menu))
                .perform(RecyclerViewActions.scrollToPosition(0))
                .check(new ViewAssertion() {
                    @Override
                    public void check(View view, NoMatchingViewException noViewFoundException) {
                        CompoundButton checkbox = view.findViewById(R.id.checkbox_menuItemSelected);
                        selectedItemNames[0] = checkbox.getText().toString();
                    }
                });

        String selectedName = selectedItemNames[0];

        // Select first food menu item
        onView(withText(selectedName)).perform(click());

        // Assert if the first item is selected
        onView(withText(selectedName)).check(matches(isChecked()));

        // Submit order
        onView(withId(R.id.btn_submit)).perform(click());

        // Assert that the selected food menu item is displayed in the summary dialog
        onView(withText(selectedName)).check(matches(isDisplayed()));

        // Close dialog
        onView(withText("Close")).perform(click());

        // Assert that the menu item is reset and not selected
        onView(withText(selectedName)).check(matches(isNotChecked()));
    }

    @Test
    public void filterMenuListTest() {
        final String[] selectedItemNames = {""};
        final boolean[] isVeggie = new boolean[1];

        // Get the name of the first order in the menu item
        onView(withId(R.id.rv_menu))
                .perform(RecyclerViewActions.scrollToPosition(0))
                .check(new ViewAssertion() {
                    @Override
                    public void check(View view, NoMatchingViewException noViewFoundException) {
                        CompoundButton checkbox = view.findViewById(R.id.checkbox_menuItemSelected);
                        selectedItemNames[0] = checkbox.getText().toString();

                        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
                        isVeggie[0] = checkbox.getCurrentTextColor() == context.getColor(R.color.colorVeggie);;
                    }
                });

        String selectedName = selectedItemNames[0];
        String filterSelection = isVeggie[0] ? "Veggie" : "Non-veggie";

        // Uncheck filter
        onView(withText(filterSelection)).perform(click());

        // Assert that the item is not displayed anymore
        onView(withText(selectedName)).check(doesNotExist());

        // Check filter again
        onView(withText(filterSelection)).perform(click());

        // Assert that the menu item is now visible
        onView(withText(selectedName)).check(matches(isDisplayed()));
    }

    @Test
    public void AddMenuItemTest() {
        // Click on 'Add' button
        onView(withId(R.id.mi_Add)).perform(click());

        // Select non-veggie option
        onView(withId(R.id.radio_nonVeggie)).perform(click());

        // Enter in the menu item name
        String newItemName = "The best fried chicken";
        onView(withId(R.id.et_foodName)).perform(replaceText(newItemName));

        // Save menu item
        onView(withId(R.id.btn_save)).perform(click());

        // Assert that the new item is added to the list
        onView(withId(R.id.rv_menu))
                .perform((RecyclerViewActions.scrollTo(hasDescendant(withText(newItemName)))));
    }
}
