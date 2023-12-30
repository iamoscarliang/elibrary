package com.oscarliang.elibrary;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.oscarliang.elibrary.di.Injectable;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class MainActivity extends AppCompatActivity implements Injectable, HasAndroidInjector {

    @Inject
    DispatchingAndroidInjector<Object> fragmentDispatchingAndroidInjector;

    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return fragmentDispatchingAndroidInjector;
    }
    //========================================================

}
