package com.oscarliang.elibrary;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.oscarliang.elibrary.di.Injectable;
import com.oscarliang.elibrary.ui.BaseFragment;
import com.oscarliang.elibrary.ui.category.CategoryFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class MainActivity extends AppCompatActivity implements Injectable, HasAndroidInjector {

    private static final String FRAGMENT_TAG = "content";

    private ProgressBar mProgressBar;

    @Inject
    DispatchingAndroidInjector<Object> fragmentDispatchingAndroidInjector;

    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

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

    @Override
    public AndroidInjector<Object> androidInjector() {
        return fragmentDispatchingAndroidInjector;
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    public void navigateToFragment(Fragment fragment) {
        navigateToFragment(fragment, 0, 0, 0, 0);
    }

    public void navigateToFragment(Fragment fragment, int enter, int exit, int popEnter, int popExit) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(enter, exit, popEnter, popExit)
                .replace(R.id.container, fragment, FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    public void showProgressBar(boolean visibility) {
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
    //========================================================

}
