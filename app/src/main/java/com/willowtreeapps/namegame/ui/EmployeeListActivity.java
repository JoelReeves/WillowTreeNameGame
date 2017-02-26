package com.willowtreeapps.namegame.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.willowtreeapps.namegame.R;

public class EmployeeListActivity extends NameGameBaseActivity {

    private static final String FRAG_TAG = "EmployeeListFragmentTag";

    public static void startEmployeeListActivity(@NonNull Activity activity) {
        Intent intent = new Intent(activity, EmployeeListActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle(R.string.employee_list_title);
    }

    @Override
    protected Fragment createFragment() {
        return EmployeeListFragment.newInstance();
    }

    @Override
    protected String getFragmentTag() {
        return FRAG_TAG;
    }
}
