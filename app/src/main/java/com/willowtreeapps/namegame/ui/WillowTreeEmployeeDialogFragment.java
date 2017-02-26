package com.willowtreeapps.namegame.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.willowtreeapps.namegame.R;
import com.willowtreeapps.namegame.network.api.model.Item;
import com.willowtreeapps.namegame.util.PicassoUtils;
import com.willowtreeapps.namegame.util.Ui;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.VISIBLE;

public class WillowTreeEmployeeDialogFragment extends DialogFragment {

    private static final String ITEM_ARG = "item_arg";
    private static final int IMAGE_SIZE = 250;
    private static final int DIALOG_WIDTH = 325;
    private static final int DIALOG_HEIGHT = 400;

    @BindView(R.id.employee_photo) ImageView employeePhoto;
    @BindView(R.id.employee_name) TextView employeeName;
    @BindView(R.id.employee_job_title) TextView employeeJobTitle;
    @BindView(R.id.close_dialog) Button closeButton;

    private Unbinder unbinder;
    private Animation fadeInAnimation;

    public WillowTreeEmployeeDialogFragment() {}

    public static WillowTreeEmployeeDialogFragment newInstance(@NonNull Item item) {
        WillowTreeEmployeeDialogFragment fragment = new WillowTreeEmployeeDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ITEM_ARG, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        fadeInAnimation.setAnimationListener(animationListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.willowtree_employee_dialog, container);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Item item = getArguments().getParcelable(ITEM_ARG);

        if (item != null) {
            PicassoUtils.loadImageFromUrl(getActivity(), item.getHeadshot().getUrl(), IMAGE_SIZE, employeePhoto);
            employeeName.setText(item.getWholeName());
            employeeJobTitle.setText(item.getJobTitle());

            employeePhoto.startAnimation(fadeInAnimation);
            employeeName.startAnimation(fadeInAnimation);
            employeeJobTitle.startAnimation(fadeInAnimation);
        }
    }

    @Override
    public void onResume() {
        Window dialogWindow = getDialog().getWindow();
        if (dialogWindow != null) {
            int dialogWidth = (int) Ui.convertDpToPixel(DIALOG_WIDTH, getContext());
            int dialogHeight = (int) Ui.convertDpToPixel(DIALOG_HEIGHT, getContext());
            dialogWindow.setLayout(dialogWidth, dialogHeight);
        }
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.close_dialog)
    protected void closeDialog() {
        dismiss();
    }

    private final Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            closeButton.setVisibility(VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    };
}
