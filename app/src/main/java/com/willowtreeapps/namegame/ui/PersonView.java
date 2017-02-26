package com.willowtreeapps.namegame.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.willowtreeapps.namegame.R;
import com.willowtreeapps.namegame.network.api.model.Item;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonView extends FrameLayout {

    public interface PersonViewClickListener {
        void onPersonClick(@NonNull PersonView personView, @NonNull Item item);
    }

    @BindView(R.id.willowtree_employee) ImageView person;
    @BindView(R.id.answer) ImageView answer;

    private Item item;
    private PersonViewClickListener personViewClickListener;

    public PersonView(Context context) {
        this(context, null);
    }

    public PersonView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        init();
    }

    @OnClick(R.id.person_container)
    protected void viewClicked() {
        if (item != null && personViewClickListener != null) {
            personViewClickListener.onPersonClick(this, item);
        }
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public ImageView getPersonImage() {
        return person;
    }

    public void showAnswer(boolean answerIsCorrect) {
        answer.setVisibility(VISIBLE);
        answer.setImageResource(answerIsCorrect ? R.drawable.ic_correct_answer : R.drawable.ic_incorrect_answer);
    }

    public void setPersonViewClickListener(PersonViewClickListener personViewClickListener) {
        this.personViewClickListener = personViewClickListener;
    }

    private void init() {
        View rootView = inflate(getContext(), R.layout.person_view, this);
        ButterKnife.bind(this, rootView);
    }
}
