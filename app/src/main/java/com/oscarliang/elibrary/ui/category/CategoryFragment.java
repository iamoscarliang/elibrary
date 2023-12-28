package com.oscarliang.elibrary.ui.category;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oscarliang.elibrary.R;
import com.oscarliang.elibrary.adapter.CategoryAdapter;
import com.oscarliang.elibrary.model.Category;
import com.oscarliang.elibrary.ui.BaseFragment;
import com.oscarliang.elibrary.ui.book.BookFragment;

public class CategoryFragment extends BaseFragment implements CategoryAdapter.OnCategoryClickListener {

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
        showSearchFragment(category.getName());
    }

    @Override
    public boolean onBackPressed() {
        showExitDialog();
        return true;
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    private void initSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showSearchFragment(query);
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

    private void showSearchFragment(String query) {
        getMainActivity().navigateToFragment(BookFragment.newInstance(query));
    }

    private void showExitDialog() {
        new ExitDialogFragment().show(getActivity().getSupportFragmentManager(), null);
    }
    //========================================================

    //--------------------------------------------------------
    // Inner Classes
    //--------------------------------------------------------
    public static class ExitDialogFragment extends DialogFragment {

        //--------------------------------------------------------
        // Overriding methods
        //--------------------------------------------------------
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.exit_dialog_title);
            builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().finish();
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            return builder.create();
        }
        //========================================================

    }
    //========================================================

}
