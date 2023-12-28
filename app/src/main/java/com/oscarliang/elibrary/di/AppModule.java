package com.oscarliang.elibrary.di;

import android.app.Application;

import androidx.room.Room;

import com.oscarliang.elibrary.api.BookService;
import com.oscarliang.elibrary.db.BookDao;
import com.oscarliang.elibrary.db.BookDatabase;
import com.oscarliang.elibrary.util.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    @Singleton
    @Provides
    BookService provideBookService() {
        return new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BookService.class);
    }

    @Singleton
    @Provides
    BookDatabase provideDb(Application application) {
        return Room
                .databaseBuilder(application, BookDatabase.class, "book.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    BookDao provideBookDao(BookDatabase bookDatabase) {
        return bookDatabase.getBookDao();
    }

}
