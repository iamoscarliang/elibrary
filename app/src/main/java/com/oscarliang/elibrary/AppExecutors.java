package com.oscarliang.elibrary;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {

    private static AppExecutors INSTANCE;

    private final Executor mDiskIO = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(3);
    private final Executor mMainThreadExecutor = new MainThreadExecutor();

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    private AppExecutors() {
    }
    //========================================================

    //--------------------------------------------------------
    // Static methods
    //--------------------------------------------------------
    public static AppExecutors getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppExecutors();
        }

        return INSTANCE;
    }
    //========================================================

    //--------------------------------------------------------
    // Getter and Setter
    //--------------------------------------------------------
    public Executor diskIO() {
        return mDiskIO;
    }

    public ScheduledExecutorService networkIO() {
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
