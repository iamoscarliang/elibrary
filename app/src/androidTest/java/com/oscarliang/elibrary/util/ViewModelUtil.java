package com.oscarliang.elibrary.util;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelUtil {

    private ViewModelUtil() {
    }

    public static <T extends ViewModel> ViewModelProvider.Factory createFor(final T model) {
        return new ViewModelProvider.Factory() {
            @Override
            public <T extends ViewModel> T create(Class<T> modelClass) {
                if (modelClass.isAssignableFrom(model.getClass())) {
                    return (T) model;
                }
                throw new IllegalArgumentException("unexpected model class " + modelClass);
            }
        };
    }

}
