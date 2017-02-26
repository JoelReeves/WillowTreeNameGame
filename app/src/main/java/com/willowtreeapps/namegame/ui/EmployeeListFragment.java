package com.willowtreeapps.namegame.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.namegame.R;
import com.willowtreeapps.namegame.core.ApplicationComponent;
import com.willowtreeapps.namegame.core.PersonService;
import com.willowtreeapps.namegame.network.api.model.Item;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EmployeeListFragment extends NameGameBaseFragment {

    private static final String EMPLOYEE_DIALOG_TAG = "employee_dialog_tag";

    @Inject PersonService personService;

    @BindView(R.id.recyclerview) RecyclerView employeeRecyclerView;

    private Unbinder unbinder;

    public static EmployeeListFragment newInstance() {
        return new EmployeeListFragment();
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.employee_list_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        employeeRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();

        populateRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_employee_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_name_game:
                NameGameActivity.startNameGameActivity(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateRecyclerView() {
        List<Item> itemList = personService.getPersonList();
        ItemListAdapter itemListAdapter = new ItemListAdapter(itemList);
        itemListAdapter.setItemClickListener(itemClickListener);

        employeeRecyclerView.setHasFixedSize(true);
        employeeRecyclerView.setAdapter(itemListAdapter);
    }

    private final ItemListAdapter.ItemClickListener itemClickListener = new ItemListAdapter.ItemClickListener() {
        @Override
        public void onClick(@NonNull Item item) {
            WillowTreeEmployeeDialogFragment fragment = WillowTreeEmployeeDialogFragment.newInstance(item);
            fragment.show(getFragmentManager(), EMPLOYEE_DIALOG_TAG);
        }
    };
}
