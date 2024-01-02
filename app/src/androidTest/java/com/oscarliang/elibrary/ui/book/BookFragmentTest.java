package com.oscarliang.elibrary.ui.book;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.oscarliang.elibrary.R;
import com.oscarliang.elibrary.model.Book;
import com.oscarliang.elibrary.testing.SingleFragmentActivity;
import com.oscarliang.elibrary.util.RecyclerViewMatcher;
import com.oscarliang.elibrary.util.TestUtil;
import com.oscarliang.elibrary.util.ViewModelUtil;
import com.oscarliang.elibrary.vo.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class BookFragmentTest {

    private BookViewModel mViewModel;

    private final MutableLiveData<Resource<List<Book>>> mResults = new MutableLiveData<>();

    @Rule
    public ActivityTestRule<SingleFragmentActivity> mActivityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

    @Before
    public void init() {
        mViewModel = mock(BookViewModel.class);
        when(mViewModel.getResults()).thenReturn(mResults);

        BookFragment fragment = new BookFragment();
        fragment.mFactory = ViewModelUtil.createFor(mViewModel);
        mActivityRule.getActivity().setFragment(fragment);
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

    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.recycle_view_book);
    }

}
