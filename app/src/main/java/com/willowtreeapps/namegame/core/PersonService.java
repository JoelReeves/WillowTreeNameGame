package com.willowtreeapps.namegame.core;

import android.support.annotation.NonNull;

import com.willowtreeapps.namegame.network.api.model.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersonService {

    private final ListRandomizer listRandomizer;
    private final ArrayList<Item> personList;

    public PersonService(ListRandomizer listRandomizer) {
        personList = new ArrayList<>();
        this.listRandomizer = listRandomizer;
    }

    public ArrayList<Item> getPersonList() {
        return personList;
    }

    public void savePeopleItemList(List<Item> people) {
        personList.clear();
        personList.addAll(people);
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
                itemSet.add(listRandomizer.pickOne(personList));
            }
        }

        List<Item> namesList = new ArrayList<>(itemSet);
        Collections.shuffle(namesList);

        for (int index = 0; index < limit; index++) {
            namesArray[index] = namesList.get(index).getWholeName();
        }

        return namesArray;
    }
}
