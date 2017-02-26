package com.willowtreeapps.namegame.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import com.squareup.picasso.Picasso;
import com.willowtreeapps.namegame.R;
import com.willowtreeapps.namegame.core.ApplicationComponent;
import com.willowtreeapps.namegame.core.ListRandomizer;
import com.willowtreeapps.namegame.core.PersonService;
import com.willowtreeapps.namegame.network.api.ProfilesRepository;
import com.willowtreeapps.namegame.network.api.model.Item;
import com.willowtreeapps.namegame.network.api.model.Profiles;
import com.willowtreeapps.namegame.util.CircleBorderTransform;
import com.willowtreeapps.namegame.util.DialogBuilder;
import com.willowtreeapps.namegame.util.NetworkUtils;
import com.willowtreeapps.namegame.util.Ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.willowtreeapps.namegame.util.DialogBuilder.showSingleMessageDialog;

public class NameGameFragment extends NameGameBaseFragment {

    private static final Interpolator OVERSHOOT = new OvershootInterpolator();
    private static final String FACES_KEY = "faces_key";
    private static final String ANSWERS_KEY = "answers_key";
    private static final String TOTAL_GUESSES_KEY = "total_answers_key";
    private static final String CORRECT_GUESSES_KEY = "correct_answers_key";
    private static final String INCORRECT_GUESSES_KEY = "incorrect_answers_key";
    private static final String FORWARD_SLASHES = "//";
    private static final String HTTP_PREFIX = "http://";
    private static final int NUMBER_OF_IMAGES = 5;
    private static final int IMAGE_SIZE = 100;

    @Inject ProfilesRepository profilesRepository;
    @Inject ListRandomizer listRandomizer;
    @Inject Picasso picasso;
    @Inject PersonService personService;
    @Inject Random random;

    @BindViews({R.id.iv_first, R.id.iv_second, R.id.iv_third, R.id.iv_fourth, R.id.iv_fifth})
    PersonView[] faces;

    @BindView(R.id.face_container) ViewGroup container;

    @BindArray(R.array.correct_answers) String[] correctAnswers;
    @BindArray(R.array.incorrect_answers) String[] incorrectAnswers;

    private Unbinder unbinder;
    private boolean answerCorrect;
    private String[] answers;
    private int totalGuesses;
    private int correctGuesses;
    private int incorrectGuesses;
    private Item chosenItem;
    private PersonView chosenPerson;
    private ArrayList<Item> retainedItems = new ArrayList<>(NUMBER_OF_IMAGES);
    private boolean[] retainedAnswerState = new boolean[NUMBER_OF_IMAGES];
    private AlertDialog guessNameDialog;
    private AlertDialog noNetworkDialog;
    private AlertDialog profileErrorDialog;
    private AlertDialog gameOverDialog;

    public static NameGameFragment newInstance() {
        return new NameGameFragment();
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
        View rootView = inflater.inflate(R.layout.name_game_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Hiding the views until data loads
        ButterKnife.apply(faces, View.SCALE_X, 0.0f);
        ButterKnife.apply(faces, View.SCALE_Y, 0.0f);

        // on initial creation, loading images from REST call
        // on re-creation restoring already retrieved Items
        if (savedInstanceState == null) {
            getProfiles();
        } else {
            retainedItems = savedInstanceState.getParcelableArrayList(FACES_KEY);
            setImages(retainedItems);

            retainedAnswerState = savedInstanceState.getBooleanArray(ANSWERS_KEY);
            totalGuesses = savedInstanceState.getInt(TOTAL_GUESSES_KEY);
            correctGuesses = savedInstanceState.getInt(CORRECT_GUESSES_KEY);
            incorrectGuesses = savedInstanceState.getInt(INCORRECT_GUESSES_KEY);

            for (int index = 0; index < NUMBER_OF_IMAGES; index++) {
                if (retainedAnswerState != null && !retainedAnswerState[index]) {
                    faces[index].enable(false);
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        profilesRepository.unregister(repositoryListener);

        dismissDialogsIfNecessary(gameOverDialog, noNetworkDialog, profileErrorDialog, guessNameDialog);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // saving Items
        for (int index = 0; index < NUMBER_OF_IMAGES; index++) {
            retainedItems.add(faces[index].getItem());
            retainedAnswerState[index] = faces[index].isEnabled();
        }

        outState.putParcelableArrayList(FACES_KEY, retainedItems);
        outState.putBooleanArray(ANSWERS_KEY, retainedAnswerState);
        outState.putInt(TOTAL_GUESSES_KEY, totalGuesses);
        outState.putInt(CORRECT_GUESSES_KEY, correctGuesses);
        outState.putInt(INCORRECT_GUESSES_KEY, incorrectGuesses);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.name_game, menu);

        // determining whether or not to show/hide the employee list menu item
        // since we don't want to go to the EmployeeListActivity without valid data to display
        if (menu != null) {
            boolean hasProfiles = !personService.getPersonList().isEmpty();
            menu.findItem(R.id.action_employee_list).setEnabled(hasProfiles).setVisible(hasProfiles);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_name_game_reload:
                reloadProfiles();
                return true;
            case R.id.action_employee_list:
                EmployeeListActivity.startEmployeeListActivity(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A method for setting the images from people into the imageviews
     */
    private void setImages(List<Item> people) {
        int imageSize = (int) Ui.convertDpToPixel(IMAGE_SIZE, getContext());

        for (int index = 0, size = faces.length; index < size; index++) {
            PersonView face = faces[index];
            face.setItem(people.get(index));
            face.setPersonViewClickListener(mPersonViewClickListener);

            final String headshotUrl = face.getItem().getHeadshot().getUrl().replace(FORWARD_SLASHES, HTTP_PREFIX);

            picasso.load(headshotUrl)
                    .placeholder(R.drawable.ic_face_white_48dp)
                    .resize(imageSize, imageSize)
                    .transform(new CircleBorderTransform())
                    .into(face.getPersonImage());
        }

        animateFacesIn();
    }

    /**
     * A method to animate the faces into view
     */
    private void animateFacesIn() {
        for (int i = 0; i < faces.length; i++) {
            PersonView face = faces[i];
            face.animate().scaleX(1).scaleY(1).setStartDelay(800 + 120 * i).setInterpolator(OVERSHOOT).start();
        }
    }

    /**
     * Method to start REST call for JSON data
     */
    private void getProfiles() {
        // checking for a valid network connection before attempting to retrieve profiles
        if (!NetworkUtils.networkIsAvailable(getActivity())) {
            noNetworkDialog = showSingleMessageDialog(getActivity(), R.string.network_error_no_network_connection, R.string.button_ok);
        } else {
            showProgressDialog();
            profilesRepository.register(repositoryListener);
        }
    }

    /**
     * Method to display an alert dialog if there is an error in retrieving the JSON data
     */
    private void showProfileErrorDialog() {
        profileErrorDialog = DialogBuilder.showDialog(getActivity(), R.string.error_title, R.string.network_error_error_retrieving_profiles,
                R.string.button_retry, R.string.button_cancel, mProfileErrorButtonClickListener);
    }

    /**
     * Method to determine if all Item's have a non-null/non-blank Headshot url
     *
     * @param itemList List of items to process
     * @return boolean value indicating whether or not the Headshot url is valid
     */
    private boolean peopleHeadshotUrlIsValid(List<Item> itemList) {
        for (Item item : itemList) {
            final String headshotUrl = item.getHeadshot().getUrl();
            if (headshotUrl == null || headshotUrl.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to inform you if your chosen answer was correct or not
     */
    private void showAnswerValidationSnackbar() {
        String answer = answerCorrect ? listRandomizer.getRandomString(correctAnswers) : listRandomizer.getRandomString(incorrectAnswers);
        int backgroundColor = answerCorrect ? ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark) : ContextCompat.getColor(getActivity(), R.color.darkRed);

        Snackbar snackbar = Snackbar.make(container, answer, BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(backgroundColor);
        snackbar.setActionTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        snackbar.show();
    }

    private boolean arePeopleStillAvailable() {
        for (PersonView personView : faces) {
            if (personView.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to reload profiles
     */
    private void reloadProfiles() {
        for (PersonView personView : faces) {
            personView.enable(true);
        }

        profilesRepository.unregister(repositoryListener);
        getProfiles();
    }

    /**
     * Method to calculate the total number of guesses and correct/incorrect guesses
     * shown at the end of the game
     */
    private void calculateGameStats() {
        totalGuesses++;

        if (answerCorrect) {
            correctGuesses++;
        }

        incorrectGuesses = totalGuesses - correctGuesses;
    }

    private void recreateMenu() {
        getActivity().invalidateOptionsMenu();
    }

    private final ProfilesRepository.Listener repositoryListener = new ProfilesRepository.Listener() {
        @Override
        public void onLoadFinished(@NonNull Profiles people) {
            dismissProgressDialog();

            if (people.getPeople().isEmpty()) {
                showProfileErrorDialog();
            } else {
                personService.savePeopleItemList(people.getPeople());
                ArrayList<Item> savedItemList = personService.getPersonList();
                List<Item> randomPeopleList = listRandomizer.pickN(savedItemList, NUMBER_OF_IMAGES);

                recreateMenu();

                while (!peopleHeadshotUrlIsValid(randomPeopleList)) {
                    randomPeopleList.clear();
                    randomPeopleList.addAll(listRandomizer.pickN(savedItemList, NUMBER_OF_IMAGES));
                }

                if (isAdded()) {
                    setImages(randomPeopleList);
                }
            }
        }

        @Override
        public void onError(@NonNull Throwable error) {
            dismissProgressDialog();
            showProfileErrorDialog();
        }
    };

    private final PersonView.PersonViewClickListener mPersonViewClickListener = new PersonView.PersonViewClickListener() {
        @Override
        public void onPersonClick(@NonNull PersonView personView, @NonNull Item item) {
            chosenItem = item;
            chosenPerson = personView;
            answers = personService.getMultipleChoicesForItem(item);

            guessNameDialog = DialogBuilder.showChooserDialog(getActivity(), R.string.question, R.string.button_ok, R.string.button_cancel, answers, answerChosenListener, confirmAnswerListener);
        }
    };

    private final DialogInterface.OnClickListener answerChosenListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            answerCorrect = answers[which].equals(chosenItem.getWholeName());
        }
    };

    private final DialogInterface.OnClickListener confirmAnswerListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (answerCorrect) {
                chosenPerson.enable(false);
            }

            calculateGameStats();

            if (arePeopleStillAvailable()) {
                showAnswerValidationSnackbar();
            } else {

                String messageText = String.format(getString(R.string.game_over_message), totalGuesses, correctGuesses, incorrectGuesses);
                gameOverDialog = DialogBuilder.showDialog(getActivity(), R.string.game_over_title, messageText, R.string.button_play_again, R.string.button_quit, gameOverClickListener);
            }
        }
    };

    private final DialogBuilder.ButtonClickListener gameOverClickListener = new DialogBuilder.ButtonClickListener() {
        @Override
        public void onPositiveClick() {
            reloadProfiles();
        }

        @Override
        public void onNegativeClick() {
            getActivity().finish();
        }
    };

    private final DialogBuilder.ButtonClickListener mProfileErrorButtonClickListener = new DialogBuilder.ButtonClickListener() {
        @Override
        public void onPositiveClick() {
            reloadProfiles();
        }

        @Override
        public void onNegativeClick() {
            dismissDialogsIfNecessary(profileErrorDialog);
            recreateMenu();
        }
    };
}
