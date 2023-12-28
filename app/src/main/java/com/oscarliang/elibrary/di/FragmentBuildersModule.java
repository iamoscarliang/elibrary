package com.oscarliang.elibrary.di;

import com.oscarliang.elibrary.ui.book.BookFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract BookFragment contributeBookFragment();

}
