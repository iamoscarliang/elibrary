package com.oscarliang.elibrary.util;

import com.oscarliang.elibrary.model.Book;
import com.oscarliang.elibrary.model.ImageLinks;
import com.oscarliang.elibrary.model.VolumeInfo;

import java.util.List;

public class TestUtil {

    public static Book createBook(String id,
                                  String title,
                                  String subtitle,
                                  List<String> authors,
                                  String publisher,
                                  String publishedDate,
                                  Float averageRating,
                                  Integer ratingsCount,
                                  Integer pageCount,
                                  String description,
                                  String previewLink,
                                  String infoLink,
                                  String thumbnail,
                                  String category) {
        VolumeInfo volumeInfo = new VolumeInfo(
                title,
                subtitle,
                authors,
                publisher,
                publishedDate,
                averageRating,
                ratingsCount,
                pageCount,
                description,
                previewLink,
                infoLink,
                new ImageLinks(thumbnail));

        return new Book(id, volumeInfo, category);
    }

}
