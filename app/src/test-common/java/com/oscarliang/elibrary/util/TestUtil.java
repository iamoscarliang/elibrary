package com.oscarliang.elibrary.util;

import com.oscarliang.elibrary.vo.Book;
import com.oscarliang.elibrary.vo.ImageLinks;
import com.oscarliang.elibrary.vo.VolumeInfo;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    public static List<Book> createBooks(int count, int id, String title, String category) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            books.add(createBook(id + i, title, category));
        }

        return books;
    }

    public static Book createBook(int id, String title, String category) {
        VolumeInfo volumeInfo = new VolumeInfo(
                title,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                new ImageLinks(null)
        );

        return new Book(String.valueOf(id), volumeInfo, category);
    }

}
