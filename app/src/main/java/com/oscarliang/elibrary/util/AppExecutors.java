package com.oscarliang.elibrary.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {

    private static AppExecutors INSTANCE;

    private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(3);

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
    public ScheduledExecutorService networkIO() {
        return mNetworkIO;
    }
    //========================================================

}
