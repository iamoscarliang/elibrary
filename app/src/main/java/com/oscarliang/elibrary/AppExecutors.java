package com.oscarliang.elibrary;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppExecutors {

    private final Executor mDiskIO;
    private final Executor mNetworkIO;
    private final Executor mMainThreadExecutor;

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public AppExecutors(Executor diskIO, Executor networkIO, Executor mainThreadExecutor) {
        mDiskIO = diskIO;
        mNetworkIO = networkIO;
        mMainThreadExecutor = mainThreadExecutor;
    }

    @Inject
    public AppExecutors() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3), new MainThreadExecutor());
    }
    //========================================================

    //--------------------------------------------------------
    // Getter and Setter
    //--------------------------------------------------------
    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor networkIO() {
        return mNetworkIO;
    }

    public Executor mainThread() {
        return mMainThreadExecutor;
    }
    //========================================================

    //--------------------------------------------------------
    // Inner classes
    //--------------------------------------------------------
    private static class MainThreadExecutor implements Executor {

        private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

        //--------------------------------------------------------
        // Overriding methods
        //--------------------------------------------------------
        @Override
        public void execute(Runnable runnable) {
            mMainThreadHandler.post(runnable);
        }
        //========================================================

    }
    //========================================================

}
