package com.willowtreeapps.namegame.network.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Profiles implements Parcelable {

    @JsonProperty("items")
    public List<Item> items;
    @JsonProperty("meta")
    public Meta meta;

    public Profiles() {}

    public Profiles(List<Item> items, Meta meta) {
        this.items = items;
        this.meta = meta;
    }

    public List<Item> getPeople() {
        return items;
    }

    public Meta getMetadata() {
        return meta;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    private Profiles(Parcel in) {
        this.items = new ArrayList<>();
        in.readList(this.items, Item.class.getClassLoader());
        this.meta = in.readParcelable(Meta.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.items);
        dest.writeParcelable(this.meta, flags);
    }

    public static final Creator<Profiles> CREATOR = new Creator<Profiles>() {
        @Override
        public Profiles createFromParcel(Parcel source) {
            return new Profiles(source);
        }

        @Override
        public Profiles[] newArray(int size) {
            return new Profiles[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Profiles{" +
                "items=" + items +
                ", meta=" + meta +
                '}';
    }
}