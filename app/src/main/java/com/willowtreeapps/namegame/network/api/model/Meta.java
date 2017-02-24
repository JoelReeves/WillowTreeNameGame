package com.willowtreeapps.namegame.network.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta implements Parcelable {

    @JsonProperty("skip")
    private Integer skip;
    @JsonProperty("limit")
    private Integer limit;
    @JsonProperty("total")
    private Integer total;

    public Meta() {}

    public Meta(int skip, int limit, int total) {
        this.skip = skip;
        this.limit = limit;
        this.total = total;
    }

    private Meta(Parcel in) {
        this.skip = in.readInt();
        this.limit = in.readInt();
        this.total = in.readInt();
    }

    public int getSkip() {
        return skip;
    }

    public int getLimit() {
        return limit;
    }

    public int getTotal() {
        return total;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.skip);
        dest.writeInt(this.limit);
        dest.writeInt(this.total);
    }

    public static final Creator<Meta> CREATOR = new Creator<Meta>() {
        @Override
        public Meta createFromParcel(Parcel source) {
            return new Meta(source);
        }

        @Override
        public Meta[] newArray(int size) {
            return new Meta[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "skip=" + skip +
                ", limit=" + limit +
                ", total=" + total +
                '}';
    }
}
