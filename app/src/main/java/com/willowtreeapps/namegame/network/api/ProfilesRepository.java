package com.willowtreeapps.namegame.network.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.willowtreeapps.namegame.core.ListRandomizer;
import com.willowtreeapps.namegame.network.api.model.Item;
import com.willowtreeapps.namegame.network.api.model.Profiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilesRepository {

    @NonNull
    private final NameGameApi api;
    @NonNull
    private final ArrayList<Item> itemList;
    @NonNull
    private List<Listener> listeners = new ArrayList<>(1);
    @NonNull
    private final ListRandomizer listRandomizer;
    @Nullable
    private Profiles profiles;

    public ProfilesRepository(@NonNull NameGameApi api, @NonNull ListRandomizer listRandomizer, Listener... listeners) {
        this.api = api;
        if (listeners != null) {
            this.listeners = new ArrayList<>(Arrays.asList(listeners));
        }
        itemList = new ArrayList<>();
        this.listRandomizer = listRandomizer;
        load();
    }

    private void load() {
        this.api.getProfiles().enqueue(new Callback<Profiles>() {
            @Override
            public void onResponse(Call<Profiles> call, Response<Profiles> response) {
                if (response.isSuccessful()) {
                    profiles = response.body();
                    for (Listener listener : listeners) {
                        listener.onLoadFinished(profiles);
                    }
                }
            }

            @Override
            public void onFailure(Call<Profiles> call, Throwable t) {
                for (Listener listener : listeners) {
                    listener.onError(t);
                }
            }
        });
    }

    public void register(@NonNull Listener listener) {
        if (listeners.contains(listener)) throw new IllegalStateException("Listener is already registered.");
        listeners.add(listener);
        if (profiles != null) {
            listener.onLoadFinished(profiles);
        }
    }

    public void unregister(@NonNull Listener listener) {
        listeners.remove(listener);
    }

    public ArrayList<Item> getItemList() {
        itemList.clear();

        if (profiles != null) {
            itemList.addAll(profiles.getPeople());
        }
        return itemList;
    }

    public boolean isEmpty() {
        return itemList.isEmpty();
    }

    /**
     * Method to get multiple choice answers
     *
     * @param item
     * @return string array with the person's name and 4 other names
     */
    public String[] getMultipleChoicesForItem(@NonNull Item item) {
        final int limit = 5;
        String[] namesArray = new String[limit];
        Set<Item> itemSet = new HashSet<>(limit);
        itemSet.add(item);

        for (int index = 1; index < limit; index++) {
            while (itemSet.size() != limit) {
                itemSet.add(listRandomizer.pickOne(itemList));
            }
        }

        List<Item> namesList = new ArrayList<>(itemSet);
        Collections.shuffle(namesList);

        for (int index = 0; index < limit; index++) {
            namesArray[index] = namesList.get(index).getWholeName();
        }

        return namesArray;
    }

    public interface Listener {
        void onLoadFinished(@NonNull Profiles people);
        void onError(@NonNull Throwable error);
    }

}
