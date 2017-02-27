package com.willowtreeapps.namegame.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.willowtreeapps.namegame.R;
import com.willowtreeapps.namegame.ui.fragments.NameGameFragment;

public class NameGameActivity extends NameGameBaseActivity {

    private static final String FRAG_TAG = "NameGameFragmentTag";

    public static void startNameGameActivity(@NonNull Activity activity) {
        Intent intent = new Intent(activity, NameGameActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle(R.string.guess_name_title);
    }

    @Override
    protected Fragment createFragment() {
        return NameGameFragment.newInstance();
    }

    @Override
    protected String getFragmentTag() {
        return FRAG_TAG;
    }
}
