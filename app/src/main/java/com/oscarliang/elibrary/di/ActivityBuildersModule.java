package com.oscarliang.elibrary.di;

import com.oscarliang.elibrary.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();
    //========================================================

}
