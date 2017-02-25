package com.willowtreeapps.namegame.network.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item implements Parcelable {

    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("jobTitle")
    private String jobTitle;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("headshot")
    private Headshot headshot;
    @JsonProperty("socialLinks")
    private List<Object> socialLinks;

    public Item() {}

    public Item(String id,
                String type,
                String slug,
                String jobTitle,
                String firstName,
                String lastName,
                Headshot headshot,
                List<Object> socialLinks) {
        this.id = id;
        this.type = type;
        this.slug = slug;
        this.jobTitle = jobTitle;
        this.firstName = firstName;
        this.lastName = lastName;
        this.headshot = headshot;
        this.socialLinks = socialLinks;
    }

    private Item(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.slug = in.readString();
        this.jobTitle = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.headshot = in.readParcelable(Headshot.class.getClassLoader());
        in.readList(socialLinks, Object.class.getClassLoader());
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSlug() {
        return slug;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getWholeName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Headshot getHeadshot() {
        return headshot;
    }

    public List<Object> getSocialLinks() {
        return socialLinks;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setHeadshot(Headshot headshot) {
        this.headshot = headshot;
    }

    public void setSocialLinks(List<Object> socialLinks) {
        this.socialLinks = socialLinks;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeString(this.slug);
        dest.writeString(this.jobTitle);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeParcelable(this.headshot, flags);
        dest.writeList(this.socialLinks);
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", slug='" + slug + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", headshot=" + headshot +
                ", socialLinks=" + socialLinks +
                '}';
    }
}