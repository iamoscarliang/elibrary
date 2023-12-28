package com.oscarliang.elibrary.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.oscarliang.elibrary.ui.book.BookViewModel;
import com.oscarliang.elibrary.viewmodel.BookViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BookViewModel.class)
    public abstract ViewModel bindBookViewModel(BookViewModel bookViewModel);

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(BookViewModelFactory viewModelFactory);

}
