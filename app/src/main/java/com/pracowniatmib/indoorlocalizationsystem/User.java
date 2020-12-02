package com.pracowniatmib.indoorlocalizationsystem;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private String id;
    private double x;
    private double y;

    public User(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }

    public String toString(){
        String out = "ID: " + id + ", x: " + x + ", y: " + y;
        return out;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeDouble(x);
        parcel.writeDouble(y);
    }

    protected User(Parcel in) {
        id = in.readString();
        x = in.readDouble();
        y = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
