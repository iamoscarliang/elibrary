package com.oscarliang.elibrary.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.oscarliang.elibrary.ui.activity.BookActivity;
import com.oscarliang.elibrary.R;
import com.oscarliang.elibrary.adapter.BookAdapter;
import com.oscarliang.elibrary.model.Book;
import com.oscarliang.elibrary.viewmodel.BookViewModel;

import java.util.List;

public class BookFragment extends BaseFragment implements BookAdapter.OnBookClickListener {

    private static final String QUERY_PARAM = "query_param";

    private String mQuery;

    private RecyclerView mRecyclerView;
    private BookAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private BookViewModel mViewModel;

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public BookFragment() {
        // Required empty public constructor
    }
    //========================================================

    //--------------------------------------------------------
    // Static methods
    //--------------------------------------------------------
    public static BookFragment newInstance(String query) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putString(QUERY_PARAM, query);
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
            mQuery = getArguments().getString(QUERY_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_book);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mViewModel = new ViewModelProvider(getActivity()).get(BookViewModel.class);
        initRecyclerView();
        initSwipeRefreshLayout();
        subscribeObservers();

        // Display books only at first launch
        if (savedInstanceState == null) {
            displayBooks();
        }
    }

    @Override
    public boolean onBackPressed() {
        // Cancel the search request
        mViewModel.cancelSearchBooks();
        getMainActivity().showProgressBar(false);
        Log.d("test", "Back pressed!");
        return super.onBackPressed();
    }

    @Override
    public void onBookClick(BookAdapter.BookViewHolder holder) {
        // Navigate to BookActivity
        Intent intent = new Intent(getContext(), BookActivity.class);
        intent.putExtra("book", mViewModel.getBooksLiveData().getValue().get(holder.getAdapterPosition()));

        // Init share element transition animation
        ImageView imageBook = holder.itemView.findViewById(R.id.image_book);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                imageBook, ViewCompat.getTransitionName(imageBook));
        startActivity(intent, options.toBundle());
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    private void initRecyclerView() {
        mAdapter = new BookAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // Check is scroll to bottom
                if (!mRecyclerView.canScrollVertically(1)
                        && !mViewModel.isPerformingQuery()
                        && !mViewModel.getQueryExhaustedLiveData().getValue()) {
                    // Load next page
                    displayNextPage();
                    Log.d("test", "Load next page!");
                }
            }
        });
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                mViewModel.cancelSearchBooks();
                mAdapter.clearBooks();
                displayBooks();
                Log.d("test", "Refresh!");
            }
        });
    }

    private void subscribeObservers() {
        mViewModel.getBooksLiveData().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                if (books != null && !books.isEmpty()) {
                    mViewModel.setPerformingQuery(false);
                    mAdapter.displayBooks(books);
                    getMainActivity().showProgressBar(false);
                }
                Log.d("test", "Books added " + (books != null ? books.size() : 0));
            }
        });

        mViewModel.getQueryExhaustedLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isQueryExhausted) {
                if (isQueryExhausted) {
                    mViewModel.setPerformingQuery(false);
                    mAdapter.displayExhausted();
                    getMainActivity().showProgressBar(false);
                    Log.d("test", "Books exhausted!");
                }
            }
        });
    }

    private void displayBooks() {
        getMainActivity().showProgressBar(true);
        // Display the first page when first search
        mViewModel.searchBooks(mQuery, 10, 0);
    }

    private void displayNextPage() {
        // Display and append the next page
        mViewModel.searchNextPageBooks();
    }
    //========================================================

}
