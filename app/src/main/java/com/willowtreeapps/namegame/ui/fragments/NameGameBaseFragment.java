package com.willowtreeapps.namegame.ui.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.willowtreeapps.namegame.core.ApplicationComponent;
import com.willowtreeapps.namegame.core.NameGameApplication;
import com.willowtreeapps.namegame.util.DialogBuilder;

public abstract class NameGameBaseFragment extends Fragment {

    protected abstract void inject(ApplicationComponent component);

    @Nullable private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inject(((NameGameApplication) getActivity().getApplication()).component());
    }

    @Override
    public void onStop() {
        super.onStop();

        dismissDialogsIfNecessary(progressDialog);
    }

    protected void showProgressDialog() {
        if (isAdded()) {
            progressDialog = DialogBuilder.showProgressDialog(getActivity(), false);
        }
    }

    protected void dismissProgressDialog() {
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected void dismissDialogsIfNecessary(Dialog... dialogs) {
        for (Dialog dialog : dialogs) {
            if (null != dialog && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
