package com.oscarliang.elibrary.util;

import com.oscarliang.elibrary.AppExecutors;

import java.util.concurrent.Executor;

public class InstantAppExecutors extends AppExecutors {

    private static final Executor INSTANT = new Executor() {
        @Override
        public void execute(Runnable runnable) {
            runnable.run();
        }
    };

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public InstantAppExecutors() {
        super(INSTANT, INSTANT, INSTANT);
    }
    //========================================================

}
