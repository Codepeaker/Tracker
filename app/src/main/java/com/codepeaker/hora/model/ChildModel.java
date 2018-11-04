package com.codepeaker.hora.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChildModel implements Parcelable {
    private String email;
    private String pwd;
    private String location;
    private String lastUpdated;

    protected ChildModel(Parcel in) {
        email = in.readString();
        pwd = in.readString();
        location = in.readString();
        lastUpdated = in.readString();
    }

    public static final Creator<ChildModel> CREATOR = new Creator<ChildModel>() {
        @Override
        public ChildModel createFromParcel(Parcel in) {
            return new ChildModel(in);
        }

        @Override
        public ChildModel[] newArray(int size) {
            return new ChildModel[size];
        }
    };

    public ChildModel() {
    }


    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(pwd);
        parcel.writeString(location);
        parcel.writeString(lastUpdated);
    }
}
