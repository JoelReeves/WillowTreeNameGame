package com.willowtreeapps.namegame.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.willowtreeapps.namegame.R;
import com.willowtreeapps.namegame.core.ApplicationComponent;
import com.willowtreeapps.namegame.core.ListRandomizer;
import com.willowtreeapps.namegame.network.api.ProfilesRepository;
import com.willowtreeapps.namegame.network.api.model.Item;
import com.willowtreeapps.namegame.network.api.model.Profiles;
import com.willowtreeapps.namegame.util.CircleBorderTransform;
import com.willowtreeapps.namegame.util.DialogBuilder;
import com.willowtreeapps.namegame.util.NetworkUtils;
import com.willowtreeapps.namegame.util.Ui;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NameGameFragment extends NameGameBaseFragment {

    private static final Interpolator OVERSHOOT = new OvershootInterpolator();

    @Inject ProfilesRepository profilesRepository;
    @Inject ListRandomizer listRandomizer;
    @Inject Picasso picasso;

    @BindView(R.id.title) TextView title;
    @BindView(R.id.face_container) ViewGroup container;

    private Unbinder unbinder;
    private Dialog noNetworkDialog;
    private Dialog profileErrorDialog;
    private List<Item> itemList = new ArrayList<>(5);
    private List<ImageView> faces = new ArrayList<>(5);

    public static NameGameFragment newInstance() {
        return new NameGameFragment();
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.name_game_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Hide the views until data loads
        title.setAlpha(0);

        int n = container.getChildCount();
        for (int i = 0; i < n; i++) {
            ImageView face = (ImageView) container.getChildAt(i);
            faces.add(face);

            //Hide the views until data loads
            face.setScaleX(0);
            face.setScaleY(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!NetworkUtils.networkIsAvailable(getActivity())) {
            noNetworkDialog = DialogBuilder.showSingleMessageDialog(getActivity(), R.string.network_error_no_network_connection, R.string.button_ok);
        } else {
            getProfiles();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        profilesRepository.unregister(repositoryListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        dismissDialogsIfNecessary(noNetworkDialog, profileErrorDialog);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * A method for setting the images from people into the imageviews
     */
    private void setImages(List<Item> people) {
        int imageSize = (int) Ui.convertDpToPixel(100, getContext());
        int n = faces.size();

        for (int i = 0; i < n; i++) {
            ImageView face = faces.get(i);

            picasso.load(people.get(i).getHeadshot().getUrl().replace("//", "http://"))
                    .placeholder(R.drawable.ic_face_white_48dp)
                    .resize(imageSize, imageSize)
                    .transform(new CircleBorderTransform())
                    .into(face);
        }

        animateFacesIn();
    }

    /**
     * A method to animate the faces into view
     */
    private void animateFacesIn() {
        title.animate().alpha(1).start();
        for (int i = 0; i < faces.size(); i++) {
            ImageView face = faces.get(i);
            face.animate().scaleX(1).scaleY(1).setStartDelay(800 + 120 * i).setInterpolator(OVERSHOOT).start();
        }
    }

    /**
     * A method to handle when a item is selected
     *
     * @param view   The view that was selected
     * @param item The item that was selected
     */
    private void onPersonSelected(@NonNull View view, @NonNull Item item) {
        //TODO evaluate whether it was the right item and make an action based on that
    }

    private void getProfiles() {
        showProgressDialog();
        profilesRepository.register(repositoryListener);
    }

    private void showProfileErrorDialog() {
        profileErrorDialog = DialogBuilder.showDialog(getActivity(), R.string.error_title, R.string.network_error_error_retrieving_profiles,
                R.string.button_retry, R.string.button_cancel, mProfileErrorButtonClickListener);
    }

    private final ProfilesRepository.Listener repositoryListener = new ProfilesRepository.Listener() {
        @Override
        public void onLoadFinished(@NonNull Profiles people) {
            dismissProgressDialog();

            List<Item> faceList = listRandomizer.pickN(people.getPeople(), 5);

            if (isAdded()) {
                setImages(faceList);
            }
        }

        @Override
        public void onError(@NonNull Throwable error) {
            dismissProgressDialog();
            showProfileErrorDialog();
        }
    };

    private final DialogBuilder.ButtonClickListener mProfileErrorButtonClickListener = new DialogBuilder.ButtonClickListener() {
        @Override
        public void onPositiveClick() {
            getProfiles();
        }

        @Override
        public void onNegativeClick() {
            dismissDialogsIfNecessary(profileErrorDialog);
        }
    };
}
