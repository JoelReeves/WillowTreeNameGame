package com.willowtreeapps.namegame.network.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Headshot implements Parcelable {

    @JsonProperty("type")
    private String type;
    @JsonProperty("mimeType")
    private String mimeType;
    @JsonProperty("id")
    private String id;
    @JsonProperty("url")
    private String url;
    @JsonProperty("alt")
    private String alt;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("width")
    private Integer width;

    public Headshot(String type,
                    String mimeType,
                    String id,
                    String url,
                    String alt,
                    int height,
                    int width) {
        this.type = type;
        this.mimeType = mimeType;
        this.id = id;
        this.url = url;
        this.alt = alt;
        this.height = height;
        this.width = width;
    }

    private Headshot(Parcel in) {
        this.type = in.readString();
        this.mimeType = in.readString();
        this.id = in.readString();
        this.url = in.readString();
        this.alt = in.readString();
        this.height = in.readInt();
        this.width = in.readInt();
    }

    public String getType() {
        return type;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getAlt() {
        return alt;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.mimeType);
        dest.writeString(this.id);
        dest.writeString(this.url);
        dest.writeString(this.alt);
        dest.writeInt(this.height);
        dest.writeInt(this.width);
    }

    public static final Creator<Headshot> CREATOR = new Creator<Headshot>() {
        @Override
        public Headshot createFromParcel(Parcel source) {
            return new Headshot(source);
        }

        @Override
        public Headshot[] newArray(int size) {
            return new Headshot[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
