package com.willowtreeapps.namegame.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import static android.view.View.NO_ID;

public final class DialogBuilder {

    private DialogBuilder() {
        throw new AssertionError("No instances.");
    }

    public static AlertDialog showDialog(@NonNull Activity activity, @StringRes int title, String message, @StringRes int positiveButton,
                                         @StringRes int negativeButton, @Nullable ButtonClickListener buttonClickListener) {

        if (activity.isFinishing()) {
            return null;
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setMessage(message);

        if (title != NO_ID) {
            dialogBuilder.setTitle(title);
        }

        if (positiveButton != NO_ID) {
            dialogBuilder.setPositiveButton(positiveButton, buttonClickListener);
        }

        if (negativeButton != NO_ID) {
            dialogBuilder.setNegativeButton(negativeButton, buttonClickListener);
        }

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        return dialog;
    }

    public static AlertDialog showDialog(@NonNull Activity activity, @StringRes int title, String message, @StringRes int positiveButton) {
        return showDialog(activity, title, message, positiveButton, NO_ID, null);
    }

    public static AlertDialog showDialog(@NonNull Activity activity, @StringRes int title, @StringRes int message, @StringRes int positiveButton,
                                         @StringRes int negativeButton, @Nullable ButtonClickListener buttonClickListener) {
        return showDialog(activity, title, activity.getString(message), positiveButton, negativeButton, buttonClickListener);
    }

    public static AlertDialog showSingleMessageDialog(@NonNull Activity activity, @StringRes int message, @StringRes int positiveButton) {
        return showDialog(activity, NO_ID, message, positiveButton, NO_ID, null);
    }

    public static ProgressDialog showProgressDialog(@NonNull Activity activity, boolean cancelable) {
        if (activity.isFinishing()) {
            return null;
        }

        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(cancelable);
        progressDialog.setIndeterminate(true);

        progressDialog.show();

        return progressDialog;
    }

    public static AlertDialog showChooserDialog(@NonNull Activity activity, @StringRes int title, @StringRes int positiveButton, @StringRes int negativeButton, @NonNull CharSequence[] values, DialogInterface.OnClickListener answerClickListener, DialogInterface.OnClickListener okClickListener) {
        if (activity.isFinishing()) {
            return null;
        }

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setSingleChoiceItems(values, -1, answerClickListener)
                .setPositiveButton(positiveButton, okClickListener)
                .setNegativeButton(negativeButton, null)
                .create();

        dialog.show();

        return dialog;
    }

    public static class ButtonClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    onPositiveClick();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    onNegativeClick();
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    onNeutralClick();
                    break;
            }
        }

        public void onPositiveClick() {}

        public void onNegativeClick() {}

        public void onNeutralClick() {}
    }
}
