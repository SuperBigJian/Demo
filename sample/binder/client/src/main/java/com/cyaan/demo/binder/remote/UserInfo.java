package com.cyaan.demo.binder.remote;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable {
    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
    private int id = 0;
    private String name = "";
    private int age = 0;
    private String desc = "I`m" + name;

    public UserInfo() {
    }

    public UserInfo(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    protected UserInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.age = in.readInt();
        this.desc = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void copyInfo(UserInfo info) {
        setId(info.id);
        setAge(info.age);
        setName(info.name);
        setDesc(info.desc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.age);
        dest.writeString(this.desc);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.age = source.readInt();
        this.desc = source.readString();
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", desc='" + desc + '\'' +
                ", hash='" + Integer.toHexString(this.hashCode()) + '\'' +
                '}';
    }
}
