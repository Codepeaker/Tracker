package com.codepeaker.hora.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ParentModel implements Parcelable {
    private String email;
    private String pwd;
    private List<String> childEmail;

    public ParentModel() {
    }

    protected ParentModel(Parcel in) {
        email = in.readString();
        pwd = in.readString();
        childEmail = in.createStringArrayList();
    }

    public static final Creator<ParentModel> CREATOR = new Creator<ParentModel>() {
        @Override
        public ParentModel createFromParcel(Parcel in) {
            return new ParentModel(in);
        }

        @Override
        public ParentModel[] newArray(int size) {
            return new ParentModel[size];
        }
    };

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

    public List<String> getChildEmail() {
        return childEmail;
    }

    public void setChildEmail(List<String> childEmail) {
        this.childEmail = childEmail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(pwd);
        parcel.writeStringList(childEmail);
    }
}
