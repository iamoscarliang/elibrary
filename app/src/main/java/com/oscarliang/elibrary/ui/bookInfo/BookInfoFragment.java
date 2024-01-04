package com.oscarliang.elibrary.ui.bookInfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.oscarliang.elibrary.R;
import com.oscarliang.elibrary.vo.Book;

import java.util.List;

public class BookInfoFragment extends Fragment {

    private static final String BOOK = "book";

    private Book mBook;

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
    // Constructors
    //--------------------------------------------------------
    public BookInfoFragment() {
        // Required empty public constructor
    }
    //========================================================

    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBook = getArguments().getParcelable(BOOK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImageBook = (ImageView) view.findViewById(R.id.image_book);
        mTextTitle = (TextView) view.findViewById(R.id.text_title);
        mTextSubtitle = (TextView) view.findViewById(R.id.text_subtitle);
        mTextAuthor = (TextView) view.findViewById(R.id.text_author);
        mTextPublisher = (TextView) view.findViewById(R.id.text_publisher);
        mTextPublishedDate = (TextView) view.findViewById(R.id.text_published_date);
        mTextRating = (TextView) view.findViewById(R.id.text_rating);
        mTextRatingCount = (TextView) view.findViewById(R.id.text_rating_count);
        mTextPageCount = (TextView) view.findViewById(R.id.text_page_count);
        mTextDescription = (TextView) view.findViewById(R.id.text_description);
        mBtnPreview = (Button) view.findViewById(R.id.btn_preview);
        mBtnInfo = (Button) view.findViewById(R.id.btn_info);

        initBook(mBook);
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    private void initBook(Book book) {
        if (book.getVolumeInfo().getImageLinks() != null) {
            Glide.with(this)
                    .load(book.getVolumeInfo().getImageLinks().getThumbnail())
                    .placeholder(R.drawable.ic_book)
                    .error(R.drawable.ic_book)
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