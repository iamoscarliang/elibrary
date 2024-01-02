package com.oscarliang.elibrary.ui.category;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.KeyEvent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.oscarliang.elibrary.R;
import com.oscarliang.elibrary.testing.SingleFragmentActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CategoryFragmentTest {

    @Rule
    public ActivityTestRule<SingleFragmentActivity> mActivityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

    @Before
    public void init() {
        CategoryFragment fragment = new CategoryFragment();
        mActivityRule.getActivity().setFragment(fragment);
    }

    @Test
    public void search() {
        onView(withId(R.id.search_view)).perform(typeText("foo"), pressKey(KeyEvent.KEYCODE_ENTER));
    }

}
