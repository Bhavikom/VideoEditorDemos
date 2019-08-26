package com.meishe.sdkdemo.edit.data;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class BitmapData {
    private static BitmapData mDataBackup;
    private ArrayList<Bitmap> mBitmapArrayList;

    public void setBitmapData(ArrayList<Bitmap> bitmapArray) {
        mBitmapArrayList = bitmapArray;
    }

    public ArrayList<Bitmap> getBitmapData() {
        return mBitmapArrayList;
    }

    public boolean replaceBitmap(int index, Bitmap bitmap) {
        if(mBitmapArrayList == null || mBitmapArrayList.size() <= index)
            return false;

        mBitmapArrayList.set(index, bitmap);
        return true;
    }

    public boolean insertBitmap(int index, Bitmap bitmap) {
        if(mBitmapArrayList == null || mBitmapArrayList.size() < index)
            return false;

        mBitmapArrayList.add(index, bitmap);
        return true;
    }

    public ArrayList<Bitmap> cloneBitmapData() {
        ArrayList<Bitmap> newList = new ArrayList<>();
        for(Bitmap bitmap:mBitmapArrayList) {
            newList.add(bitmap);
        }
        return newList;
    }

    public void clear() {
        mBitmapArrayList.clear();
    }

    public static BitmapData init() {
        if (mDataBackup == null) {
            synchronized (BitmapData.class){
                if (mDataBackup == null) {
                    mDataBackup = new BitmapData();
                }
            }
        }
        return mDataBackup;
    }

    public static BitmapData instance() {
        if (mDataBackup == null)
            mDataBackup = new BitmapData();
        return mDataBackup;
    }

    private BitmapData() {
        mBitmapArrayList = new ArrayList<>();
    }
}
