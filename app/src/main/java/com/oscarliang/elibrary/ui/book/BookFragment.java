package com.oscarliang.elibrary.ui.book;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.oscarliang.elibrary.R;
import com.oscarliang.elibrary.di.Injectable;
import com.oscarliang.elibrary.model.Book;
import com.oscarliang.elibrary.vo.Resource;

import java.util.List;

import javax.inject.Inject;

public class BookFragment extends Fragment implements Injectable, BookAdapter.OnBookClickListener {

    private static final String QUERY = "query";

    private String mQuery;

    @Inject
    ViewModelProvider.Factory mFactory;

    private RecyclerView mRecyclerView;
    private BookAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    private BookViewModel mViewModel;

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public BookFragment() {
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
            mQuery = getArguments().getString(QUERY);
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
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mViewModel = new ViewModelProvider(getActivity(), mFactory).get(BookViewModel.class);
        initRecyclerView();
        initSwipeRefreshLayout();
        subscribeObservers();

        // Load books only at first launch
        if (savedInstanceState == null) {
            loadBooks();
        }
    }

    @Override
    public void onBookClick(Book book) {
        navigateBookInfoFragment(book);
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
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastPosition = layoutManager.findLastVisibleItemPosition();
                // Check is scroll to last item
                if (lastPosition == mAdapter.getItemCount() - 1) {
                    loadNextPage();
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
                loadBooks();
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
                        mAdapter.showBooks(listResource.mData);
                        showProgressBar(false);
                        break;
                    case ERROR:
                        mAdapter.showError();
                        showProgressBar(false);
                        Toast.makeText(getContext(), "No network connection!", Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        if (listResource.mData != null && !listResource.mData.isEmpty()) {
                            mAdapter.showBooks(listResource.mData);
                            showProgressBar(false);
                        }
                        break;
                }
            }
        });
    }

    private void navigateBookInfoFragment(Book book) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("book", book);
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_bookFragment_to_bookInfoFragment, bundle);
    }

    private void loadBooks() {
        // Load the first page when first search
        mViewModel.setQuery(mQuery, 10, 1);
        showProgressBar(true);
    }

    private void loadNextPage() {
        // Load and append the next page
        mViewModel.loadNextPage();
    }

    public void showProgressBar(boolean visibility) {
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
    //========================================================

}
