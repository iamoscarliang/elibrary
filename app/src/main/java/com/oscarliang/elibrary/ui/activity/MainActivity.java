package com.oscarliang.elibrary.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.oscarliang.elibrary.R;
import com.oscarliang.elibrary.ui.fragment.BaseFragment;
import com.oscarliang.elibrary.ui.fragment.BookFragment;
import com.oscarliang.elibrary.ui.fragment.CategoryFragment;

public class MainActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "content";

    private SearchView mSearchView;
    private ProgressBar mProgressBar;

    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        initSearchView();

        // Display category only at first launch
        if (savedInstanceState == null) {
            navigateToFragment(new CategoryFragment());
        }
    }

    @Override
    public void onBackPressed() {
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null || !fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    public void navigateToFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    public void showProgressBar(boolean visibility) {
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void initSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                navigateToFragment(BookFragment.newInstance(query));
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    //========================================================

}
