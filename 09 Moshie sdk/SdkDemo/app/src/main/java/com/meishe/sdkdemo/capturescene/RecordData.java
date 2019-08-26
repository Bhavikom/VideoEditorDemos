package com.meishe.sdkdemo.capturescene;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CaoZhiChao on 2018/11/13 16:37
 */
public class RecordData implements Parcelable{
    private long length;
    private String path;

    public RecordData(long length, String path) {
        this.length = length;
        this.path = path;
    }

    protected RecordData(Parcel in) {
        length = in.readLong();
        path = in.readString();
    }

    public static final Creator<RecordData> CREATOR = new Creator<RecordData>() {
        @Override
        public RecordData createFromParcel(Parcel in) {
            return new RecordData(in);
        }

        @Override
        public RecordData[] newArray(int size) {
            return new RecordData[size];
        }
    };

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
     * @see #CONTENTS_FILE_DESCRIPTOR
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
        dest.writeLong(length);
        dest.writeString(path);
    }
}
