package com.meishe.sdkdemo.main;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CaoZhiChao on 2018/11/14 20:29
 */
public class MainViewPagerFragmentData implements Parcelable {
    private int backGroundId;
    private String name;
    private int imageId;

    public MainViewPagerFragmentData(int backGroundId, String name, int imageId) {
        this.backGroundId = backGroundId;
        this.name = name;
        this.imageId = imageId;
    }

    protected MainViewPagerFragmentData(Parcel in) {
        backGroundId = in.readInt();
        name = in.readString();
        imageId = in.readInt();
    }

    public static final Creator<MainViewPagerFragmentData> CREATOR = new Creator<MainViewPagerFragmentData>() {
        @Override
        public MainViewPagerFragmentData createFromParcel(Parcel in) {
            return new MainViewPagerFragmentData(in);
        }

        @Override
        public MainViewPagerFragmentData[] newArray(int size) {
            return new MainViewPagerFragmentData[size];
        }
    };

    public int getBackGroundId() {
        return backGroundId;
    }

    public void setBackGroundId(int backGroundId) {
        this.backGroundId = backGroundId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(backGroundId);
        dest.writeString(name);
        dest.writeInt(imageId);
    }
}
