package com.oscarliang.elibrary.ui.fragment;

import androidx.fragment.app.Fragment;

import com.oscarliang.elibrary.ui.activity.MainActivity;

public class BaseFragment extends Fragment {

    //--------------------------------------------------------
    // Getter and Setter
    //--------------------------------------------------------
    protected MainActivity getMainActivity() {
        return ((MainActivity) getActivity());
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    public boolean onBackPressed() {
        return false;
    }
    //========================================================

}
