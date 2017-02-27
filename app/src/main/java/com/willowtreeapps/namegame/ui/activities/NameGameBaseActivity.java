package com.willowtreeapps.namegame.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.willowtreeapps.namegame.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class NameGameBaseActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();
    protected abstract String getFragmentTag();

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // using findFragmentByTag to avoid fragment recreation if activity is destroyed & re-created
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(getFragmentTag());

        if (fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment, getFragmentTag())
                    .commit();
        }
    }

    protected void setActionBarTitle(@StringRes int title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
