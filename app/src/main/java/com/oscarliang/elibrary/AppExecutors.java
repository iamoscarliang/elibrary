package com.oscarliang.elibrary;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppExecutors {

    private final Executor mDiskIO = Executors.newSingleThreadExecutor();
    private final Executor mNetworkIO = Executors.newFixedThreadPool(3);
    private final Executor mMainThreadExecutor = new MainThreadExecutor();

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    @Inject
    public AppExecutors() {
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
