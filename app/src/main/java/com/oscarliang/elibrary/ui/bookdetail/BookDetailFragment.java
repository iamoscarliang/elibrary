package com.oscarliang.elibrary.ui.bookdetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.oscarliang.elibrary.R;
import com.oscarliang.elibrary.model.Book;
import com.oscarliang.elibrary.ui.BaseFragment;

import java.util.List;

public class BookDetailFragment extends BaseFragment {

    private static final String BOOK_PARAM = "book_param";

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
    public BookDetailFragment() {
        // Required empty public constructor
    }
    //========================================================

    //--------------------------------------------------------
    // Static methods
    //--------------------------------------------------------
    public static BookDetailFragment newInstance(Book book) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(BOOK_PARAM, book);
        fragment.setArguments(args);
        return fragment;
    }
    //========================================================

    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBook = getArguments().getParcelable(BOOK_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_detail, container, false);
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