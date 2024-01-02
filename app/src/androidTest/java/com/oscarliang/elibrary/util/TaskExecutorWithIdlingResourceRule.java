package com.oscarliang.elibrary.util;

import androidx.arch.core.executor.testing.CountingTaskExecutorRule;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;

import org.junit.runner.Description;

import java.util.concurrent.CopyOnWriteArrayList;

public class TaskExecutorWithIdlingResourceRule extends CountingTaskExecutorRule {

    private CopyOnWriteArrayList<IdlingResource.ResourceCallback> callbacks = new CopyOnWriteArrayList<>();

    @Override
    protected void starting(Description description) {
        Espresso.registerIdlingResources(new IdlingResource() {
            @Override
            public String getName() {
                return "architecture components idling resource";
            }

            @Override
            public boolean isIdleNow() {
                return TaskExecutorWithIdlingResourceRule.this.isIdle();
            }

            @Override
            public void registerIdleTransitionCallback(ResourceCallback callback) {
                callbacks.add(callback);
            }
        });
        super.starting(description);
    }

    @Override
    protected void onIdle() {
        super.onIdle();
        for (IdlingResource.ResourceCallback callback : callbacks) {
            callback.onTransitionToIdle();
        }
    }

}
