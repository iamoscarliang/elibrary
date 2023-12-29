package com.oscarliang.elibrary.repository;

import androidx.lifecycle.LiveData;

import com.oscarliang.elibrary.AppExecutors;
import com.oscarliang.elibrary.api.ApiResponse;
import com.oscarliang.elibrary.api.BookResponse;
import com.oscarliang.elibrary.api.BookService;
import com.oscarliang.elibrary.db.BookDao;
import com.oscarliang.elibrary.model.Book;
import com.oscarliang.elibrary.vo.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BookRepository {

    private final AppExecutors mExecutors;
    private final BookDao mDao;
    private final BookService mBookService;

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    @Inject
    public BookRepository(AppExecutors appExecutors, BookDao bookDao, BookService bookService) {
        mExecutors = appExecutors;
        mDao = bookDao;
        mBookService = bookService;
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    public LiveData<Resource<List<Book>>> getBooks(String query, int maxResults, int page) {
        return new NetworkBoundResource<List<Book>, BookResponse>(mExecutors) {
            @Override
            protected void saveCallResult(BookResponse item) {
                List<Book> books = item.getBooks();
                if (books != null) {
                    for (Book b : books) {
                        b.setCategory(query);
                    }
                    mDao.insertBooks(books);
                }
            }

            @Override
            protected boolean shouldFetch(List<Book> data) {
                return data.size() < maxResults * page;
            }

            @Override
            protected LiveData<List<Book>> loadFromDb() {
                return mDao.searchBooks(query, maxResults * page);
            }

            @Override
            protected LiveData<ApiResponse<BookResponse>> createCall() {
                return mBookService.getBooks(query, String.valueOf(maxResults), String.valueOf(maxResults * (page - 1)));
            }
        }.getLiveData();
    }
    //========================================================

}
