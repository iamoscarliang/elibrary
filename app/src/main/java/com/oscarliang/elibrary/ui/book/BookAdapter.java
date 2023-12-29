package com.oscarliang.elibrary.ui.book;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oscarliang.elibrary.R;
import com.oscarliang.elibrary.model.Book;
import com.oscarliang.elibrary.ui.common.GenericViewHolder;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BOOK_TYPE = 0;
    private static final int LOADING_TYPE = 1;
    private static final int EXHAUSTED_TYPE = 2;
    private static final int ERROR_TYPE = 3;

    private final OnBookClickListener mOnBookClickListener;

    private LastItemState mLastItemState = LastItemState.NONE;

    private final List<Book> mBooks = new ArrayList<>();

    private enum LastItemState {
        NONE,
        LOADING,
        EXHAUSTED,
        ERROR
    }

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public BookAdapter(OnBookClickListener onBookClickListener) {
        mOnBookClickListener = onBookClickListener;
    }
    //========================================================

    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view;
        switch (viewType) {
            case BOOK_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book_item, parent, false);
                return new BookViewHolder(view, mOnBookClickListener);
            case LOADING_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
                return new GenericViewHolder(view);
            case EXHAUSTED_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_exhausted_item, parent, false);
                return new GenericViewHolder(view);
            case ERROR_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_error_item, parent, false);
                return new GenericViewHolder(view);
            default:
                throw new IllegalArgumentException("ViewHolder type not found!");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Init view holder
        int viewType = getItemViewType(position);
        if (viewType == BOOK_TYPE) {
            Book book = mBooks.get(position);
            if (book.getVolumeInfo().getImageLinks() != null) {
                Glide.with(holder.itemView.getContext())
                        .load(book.getVolumeInfo().getImageLinks().getThumbnail())
                        .placeholder(R.drawable.ic_book)
                        .error(R.drawable.ic_book)
                        .into(((BookViewHolder) holder).mImageBook);
            }
            ((BookViewHolder) holder).mTxtTitle.setText(book.getVolumeInfo().getTitle());
            ((BookViewHolder) holder).mTxtSubtitle.setText(book.getVolumeInfo().getSubtitle());
            List<String> author = book.getVolumeInfo().getAuthors();
            ((BookViewHolder) holder).mTxtAuthor.setText("By: " + (author != null ? author.toString().
                    replace("[", "").replace("]", "") : "Unknown Author"));
            Float rating = book.getVolumeInfo().getAverageRating();
            ((BookViewHolder) holder).mTxtRating.setText((rating != null ? String.valueOf(rating) : 0) + "/5");
            Integer ratingCount = book.getVolumeInfo().getRatingsCount();
            ((BookViewHolder) holder).mTxtRatingCount.setText("Reviewer: " + (ratingCount != null ? String.valueOf(ratingCount) : 0));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mBooks.size()) {
            // Update last item view base on current state
            switch (mLastItemState) {
                case LOADING:
                    return LOADING_TYPE;
                case EXHAUSTED:
                    return EXHAUSTED_TYPE;
                case ERROR:
                    return ERROR_TYPE;
                default:
                    throw new IllegalArgumentException("Last item type not found!");
            }
        } else {
            return BOOK_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        // Add one extra item to display loading or exhausted view
        return mBooks.size() + (mLastItemState != LastItemState.NONE ? 1 : 0);
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    public void showBooks(List<Book> books) {
        mBooks.clear();
        mBooks.addAll(books);
        // Update last item state
        if (books.size() < 10) {
            mLastItemState = LastItemState.EXHAUSTED;
        } else {
            mLastItemState = LastItemState.LOADING;
        }
        notifyDataSetChanged();
    }

    public void showError() {
        mLastItemState = LastItemState.ERROR;
        notifyDataSetChanged();
    }

    public void clearBooks() {
        mBooks.clear();
        mLastItemState = LastItemState.NONE;
        notifyDataSetChanged();
    }
    //========================================================

    //--------------------------------------------------------
    // Inner Classes
    //--------------------------------------------------------
    private class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnBookClickListener mOnBookClickListener;
        private final ImageView mImageBook;
        private final TextView mTxtTitle;
        private final TextView mTxtSubtitle;
        private final TextView mTxtAuthor;
        private final TextView mTxtRating;
        private final TextView mTxtRatingCount;

        //--------------------------------------------------------
        // Constructors
        //--------------------------------------------------------
        public BookViewHolder(View view, OnBookClickListener onBookClickListener) {
            super(view);
            mOnBookClickListener = onBookClickListener;
            mImageBook = view.findViewById(R.id.image_book);
            mTxtTitle = view.findViewById(R.id.text_title);
            mTxtSubtitle = view.findViewById(R.id.text_subtitle);
            mTxtAuthor = view.findViewById(R.id.text_author);
            mTxtRating = view.findViewById(R.id.text_rating);
            mTxtRatingCount = view.findViewById(R.id.text_rating_count);
            view.setOnClickListener(this);
        }
        //========================================================

        //--------------------------------------------------------
        // Overriding methods
        //--------------------------------------------------------
        @Override
        public void onClick(View view) {
            mOnBookClickListener.onBookClick(mBooks.get(getAdapterPosition()));
        }
        //========================================================

    }

    public interface OnBookClickListener {

        void onBookClick(Book book);

    }
    //========================================================

}
