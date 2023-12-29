package com.oscarliang.elibrary.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oscarliang.elibrary.R;
import com.oscarliang.elibrary.model.Category;

public class CategoryFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener {

    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public CategoryFragment() {
        // Required empty public constructor
    }
    //========================================================

    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                showExitDialog();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchView = (SearchView) view.findViewById(R.id.search_view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_category);
        initSearchView();
        initRecyclerView();
    }

    @Override
    public void onCategoryClick(Category category) {
        navigateBookFragment(category.getName());
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    private void initSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                navigateBookFragment(query);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new CategoryAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void navigateBookFragment(String query) {
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_categoryFragment_to_bookFragment, bundle);

    }

    private void showExitDialog() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_categoryFragment_to_exitDialogFragment);
    }
    //========================================================

}
