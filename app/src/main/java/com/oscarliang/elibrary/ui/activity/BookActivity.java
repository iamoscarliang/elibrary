package com.oscarliang.elibrary.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.oscarliang.elibrary.R;
import com.oscarliang.elibrary.model.Book;

import java.util.List;

public class BookActivity extends AppCompatActivity {

    private ImageView mImageBook;
    private TextView mTextTitle;
    private TextView mTextSubtitle;
    private TextView mTextAuthor;
    private TextView mTextPublisher;
    private TextView mTextPublishedDate;
    private TextView mTextRating;
    private TextView mTextRatingCount;
    private TextView mTextPageCount;
    private TextView mTextDescription;
    private Button mBtnPreview;
    private Button mBtnInfo;

    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        mImageBook = (ImageView) findViewById(R.id.image_book);
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mTextSubtitle = (TextView) findViewById(R.id.text_subtitle);
        mTextAuthor = (TextView) findViewById(R.id.text_author);
        mTextPublisher = (TextView) findViewById(R.id.text_publisher);
        mTextPublishedDate = (TextView) findViewById(R.id.text_published_date);
        mTextRating = (TextView) findViewById(R.id.text_rating);
        mTextRatingCount = (TextView) findViewById(R.id.text_rating_count);
        mTextPageCount = (TextView) findViewById(R.id.text_page_count);
        mTextDescription = (TextView) findViewById(R.id.text_description);
        mBtnPreview = (Button) findViewById(R.id.btn_preview);
        mBtnInfo = (Button) findViewById(R.id.btn_info);

        if (getIntent().hasExtra("book")) {
            Book book = getIntent().getParcelableExtra("book");
            initBook(book);
        }
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    private void initBook(Book book) {
        if (book.getVolumeInfo().getImageLinks() != null) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_book);
            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(book.getVolumeInfo().getImageLinks().getThumbnail())
                    .into(mImageBook);
        }
        mTextTitle.setText(book.getVolumeInfo().getTitle());
        mTextSubtitle.setText(book.getVolumeInfo().getSubtitle());
        List<String> author = book.getVolumeInfo().getAuthors();
        mTextAuthor.setText("By: " + (author != null ? author.toString().
                replace("[", "").replace("]", "") : "Unknown Author"));
        String publish = book.getVolumeInfo().getPublisher();
        mTextPublisher.setText("Publisher: " + (publish != null ? publish : "Unknown"));
        String publishedDate = book.getVolumeInfo().getPublishedDate();
        mTextPublishedDate.setText("Date: " + (publishedDate != null ? publishedDate : "Unknown"));
        Float rating = book.getVolumeInfo().getAverageRating();
        mTextRating.setText((rating != null ? String.valueOf(rating) : 0) + "/5");
        Integer ratingCount = book.getVolumeInfo().getRatingsCount();
        mTextRatingCount.setText("Reviewer: " + (ratingCount != null ? String.valueOf(ratingCount) : 0));
        Integer pageCount = book.getVolumeInfo().getPageCount();
        mTextPageCount.setText("Page: " + (pageCount != null ? String.valueOf(pageCount) : "Unknown"));
        String description = book.getVolumeInfo().getDescription();
        mTextDescription.setText("Description:\n" + (description != null ? description : "No description available!"));

        mBtnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String previewLink = book.getVolumeInfo().getPreviewLink();
                Uri uri = Uri.parse(previewLink);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        mBtnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String infoLink = book.getVolumeInfo().getInfoLink();
                Uri uri = Uri.parse(infoLink);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
    //========================================================

}
