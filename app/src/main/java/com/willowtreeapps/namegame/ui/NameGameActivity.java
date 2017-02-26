package com.willowtreeapps.namegame.ui;

import android.support.v4.app.Fragment;

import com.willowtreeapps.namegame.core.ApplicationComponent;

public class NameGameActivity extends NameGameBaseActivity {

    private static final String FRAG_TAG = "NameGameFragmentTag";

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
