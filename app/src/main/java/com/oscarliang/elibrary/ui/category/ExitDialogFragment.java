package com.oscarliang.elibrary.ui.category;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.oscarliang.elibrary.R;

public class ExitDialogFragment extends DialogFragment {

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
