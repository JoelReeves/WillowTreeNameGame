package com.willowtreeapps.namegame.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.willowtreeapps.namegame.R;
import com.willowtreeapps.namegame.network.api.model.Item;

public class GuessNameDialogFragment extends DialogFragment {

    public interface GuessAnswerListener {
        void onAnswerChosen(boolean isAnswerCorrect);
    }

    private static final String ITEM_ARG = "item_arg";
    private static final String ANSWERS_ARG = "answers_arg";

    private Item item;
    private String[] answers;
    private boolean answerCorrect;
    private GuessAnswerListener guessAnswerListener;

    public GuessNameDialogFragment() {}

    public static GuessNameDialogFragment newInstance(@NonNull Item item, @NonNull String[] answers) {
        GuessNameDialogFragment fragment = new GuessNameDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEM_ARG, item);
        bundle.putStringArray(ANSWERS_ARG, answers);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        item = getArguments().getParcelable(ITEM_ARG);
        answers = getArguments().getStringArray(ANSWERS_ARG);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.question);
        builder.setSingleChoiceItems(answers, -1, answerChosenListener);
        builder.setPositiveButton(R.string.button_ok, confirmAnswerListener);
        builder.setNegativeButton(R.string.button_cancel, null);
        return builder.create();
    }

    private final DialogInterface.OnClickListener answerChosenListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            answerCorrect = answers[which].equals(item.getWholeName());
        }
    };

    private final DialogInterface.OnClickListener confirmAnswerListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (guessAnswerListener != null) {
                guessAnswerListener.onAnswerChosen(answerCorrect);
            }
        }
    };

    public void setGuessAnswerListener(GuessAnswerListener guessAnswerListener) {
        this.guessAnswerListener = guessAnswerListener;
    }
}
