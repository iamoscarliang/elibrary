package com.oscarliang.elibrary.ui.book;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.oscarliang.elibrary.R;
import com.oscarliang.elibrary.vo.Book;
import com.oscarliang.elibrary.util.RecyclerViewMatcher;
import com.oscarliang.elibrary.util.TestUtil;
import com.oscarliang.elibrary.util.ViewModelUtil;
import com.oscarliang.elibrary.vo.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class BookFragmentTest {

    private NavController navController;
    private BookViewModel mViewModel;

    private final MutableLiveData<Resource<List<Book>>> mResults = new MutableLiveData<>();

    @Before
    public void init() {
        navController = mock(NavController.class);
        mViewModel = mock(BookViewModel.class);
        when(mViewModel.getResults()).thenReturn(mResults);

        FragmentScenario<BookFragment> scenario = FragmentScenario.launchInContainer(BookFragment.class, null,
                new FragmentFactory() {
                    @Override
                    public Fragment instantiate(ClassLoader classLoader, String className) {
                        BookFragment fragment = new BookFragment();
                        fragment.mFactory = ViewModelUtil.createFor(mViewModel);
                        return fragment;
                    }
                });
        scenario.onFragment(new FragmentScenario.FragmentAction<BookFragment>() {
            @Override
            public void perform(BookFragment fragment) {
                Navigation.setViewNavController(fragment.getView(), navController);
            }
        });
    }

    @Test
    public void testLoading() {
        mResults.postValue(Resource.loading(null));
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
    }

    @Test
    public void loadResults() {
        Book book = TestUtil.createBook(0, "foo", "bar");
        mResults.postValue(Resource.success(Arrays.asList(book)));
        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("foo"))));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
    }

    @Test
    public void dataWithLoading() {
        Book book = TestUtil.createBook(0, "foo", "bar");
        mResults.postValue(Resource.loading(Arrays.asList(book)));
        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("foo"))));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
    }

    @Test
    public void error() {
        mResults.postValue(Resource.error("Failed to load!", null));
        onView(withId(R.id.text_error)).check(matches(isDisplayed()));
    }

    @Test
    public void loadMore() {
        List<Book> books = TestUtil.createBooks(10, 0, "foo", "bar");
        mResults.postValue(Resource.success(books));
        ViewAction action = RecyclerViewActions.scrollToPosition(10);
        onView(withId(R.id.recycle_view_book)).perform(action);
        onView(listMatcher().atPosition(10)).check(matches(isDisplayed()));
        verify(mViewModel).loadNextPage();
    }

    @Test
    public void navigateToBookInfo() {
        doNothing().when(mViewModel).loadNextPage();
        Book book = TestUtil.createBook(0, "foo", "bar");
        mResults.postValue(Resource.loading(Arrays.asList(book)));
        onView(withText("foo")).perform(click());
        verify(navController).navigate(eq(R.id.action_bookFragment_to_bookInfoFragment), any(Bundle.class));
    }

    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.recycle_view_book);
    }

}
