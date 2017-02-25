package com.willowtreeapps.namegame.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.willowtreeapps.namegame.R;
import com.willowtreeapps.namegame.core.NameGameApplication;

public class NameGameActivity extends AppCompatActivity {

    private static final String FRAG_TAG = "NameGameFragmentTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_game_activity);
        NameGameApplication.get(this).component().inject(this);

        // using findFragmentByTag to avoid fragment recreation if activity is destroyed & re-created
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(FRAG_TAG);

        if (fragment == null) {
            fragment = NameGameFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment, FRAG_TAG)
                    .commit();
        }
    }
}
