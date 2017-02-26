package com.willowtreeapps.namegame.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.willowtreeapps.namegame.R;
import com.willowtreeapps.namegame.core.ApplicationComponent;
import com.willowtreeapps.namegame.core.NameGameApplication;

import butterknife.ButterKnife;

public abstract class NameGameBaseActivity extends AppCompatActivity {

    protected abstract void inject(ApplicationComponent component);
    protected abstract Fragment createFragment();
    protected abstract String getFragmentTag();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        ButterKnife.bind(this);

        inject(((NameGameApplication) getApplication()).component());

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
}
