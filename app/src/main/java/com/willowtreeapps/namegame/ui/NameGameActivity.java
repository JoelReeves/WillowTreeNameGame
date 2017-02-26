package com.willowtreeapps.namegame.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.willowtreeapps.namegame.R;
import com.willowtreeapps.namegame.core.ApplicationComponent;

public class NameGameActivity extends NameGameBaseActivity {

    private static final String FRAG_TAG = "NameGameFragmentTag";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle(R.string.guess_name_title);
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
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
