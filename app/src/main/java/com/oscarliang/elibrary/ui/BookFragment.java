package com.oscarliang.elibrary.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.oscarliang.elibrary.R;
import com.oscarliang.elibrary.adapter.BookAdapter;
import com.oscarliang.elibrary.model.Book;
import com.oscarliang.elibrary.viewmodel.BookViewModel;
import com.oscarliang.elibrary.vo.Resource;

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
        getMainActivity().showProgressBar(false);
        Log.d("test", "Back pressed!");
        return super.onBackPressed();
    }

    @Override
    public void onBookClick(Book book) {
        getMainActivity().navigateToFragment(BookDetailFragment.newInstance(book),
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
        );
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
                if (!mRecyclerView.canScrollVertically(1)) {
                    // Load next page
                    displayNextPage();
                }
            }
        });
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.clearBooks();
                displayBooks();
                Log.d("test", "Refresh!");
            }
        });
    }

    private void subscribeObservers() {
        mViewModel.getResults().observe(getViewLifecycleOwner(), new Observer<Resource<List<Book>>>() {
            @Override
            public void onChanged(Resource<List<Book>> listResource) {
                switch (listResource.mState) {
                    case SUCCESS:
                        mAdapter.displayBooks(listResource.mData);
                        getMainActivity().showProgressBar(false);
                        break;
                    case ERROR:
                        mAdapter.displayError();
                        getMainActivity().showProgressBar(false);
                        Toast.makeText(getContext(), "No network connection!", Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        // Ignore
                        break;
                }
            }
        });
    }

    private void displayBooks() {
        // Display the first page when first search
        mViewModel.setQuery(mQuery, 10, 1);
        getMainActivity().showProgressBar(true);
    }

    private void displayNextPage() {
        // Display and append the next page
        mViewModel.loadNextPage();
    }
    //========================================================

}
