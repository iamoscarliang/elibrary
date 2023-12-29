package com.oscarliang.elibrary.ui.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.oscarliang.elibrary.R;
import com.oscarliang.elibrary.model.Category;
import com.oscarliang.elibrary.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final OnCategoryClickListener mOnCategoryClickListener;

    private final List<Category> mCategories = new ArrayList<>();

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public CategoryAdapter(OnCategoryClickListener onCategoryClickListener) {
        mOnCategoryClickListener = onCategoryClickListener;
        initCategory();
    }
    //========================================================

    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_item, parent, false);
        return new CategoryViewHolder(view, mOnCategoryClickListener);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category category = mCategories.get(position);
        holder.mImageCategory.setImageResource(category.getThumbnail());
        holder.mTxtCategory.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    private void initCategory() {
        for (int i = 0; i < Constant.DEFAULT_SEARCH_CATEGORIES.length; i++) {
            Category category = new Category(Constant.DEFAULT_SEARCH_CATEGORIES[i],
                    Constant.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            mCategories.add(category);
        }
    }
    //========================================================

    //--------------------------------------------------------
    // Inner Classes
    //--------------------------------------------------------
    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnCategoryClickListener mOnCategoryClickListener;
        private final ImageView mImageCategory;
        private final TextView mTxtCategory;

        //--------------------------------------------------------
        // Constructors
        //--------------------------------------------------------
        public CategoryViewHolder(View view, OnCategoryClickListener onCategoryClickListener) {
            super(view);
            mOnCategoryClickListener = onCategoryClickListener;
            mImageCategory = view.findViewById(R.id.image_category);
            mTxtCategory = view.findViewById(R.id.text_category);
            view.setOnClickListener(this);
        }
        //========================================================

        //--------------------------------------------------------
        // Overriding methods
        //--------------------------------------------------------
        @Override
        public void onClick(View view) {
            mOnCategoryClickListener.onCategoryClick(mCategories.get(getAdapterPosition()));
        }
        //========================================================

    }

    public interface OnCategoryClickListener {

        void onCategoryClick(Category category);

    }
    //========================================================

}
