package com.willowtreeapps.namegame.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.willowtreeapps.namegame.core.ApplicationComponent;
import com.willowtreeapps.namegame.core.NameGameApplication;

public abstract class NameGameBaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inject(((NameGameApplication) getActivity().getApplication()).component());
    }

    protected abstract void inject(ApplicationComponent component);
}
