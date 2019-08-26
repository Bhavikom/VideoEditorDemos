package com.meishe.sdkdemo.edit.data;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.meishe.sdkdemo.utils.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by admin on 2018/11/28.
 */
public class ParseJsonFile {
    private static final String TAG = "ParseJsonFile";

    /**
     * Json转Java对象
     */
    public static <T> T fromJson(String json, Class<T> clz) {
        return new Gson().fromJson(json, clz);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return new Gson().fromJson(json, typeOfT);
    }

    public static ArrayList<FxJsonFileInfo.JsonFileInfo> readBundleFxJsonFile(Context context, String jsonFilePath) {
        String retsult = readAssetJsonFile(context, jsonFilePath);
        if (TextUtils.isEmpty(retsult)) {
            return null;
        }
        FxJsonFileInfo resultInfo = fromJson(retsult, FxJsonFileInfo.class);
        ArrayList<FxJsonFileInfo.JsonFileInfo> infoLists = resultInfo.getFxInfoList();
        return infoLists;
    }

    public static String readAssetJsonFile(Context context, String jsonFilePath) {
        if (context == null) {
            return null;
        }
        if (TextUtils.isEmpty(jsonFilePath)) {
            return null;
        }
        BufferedReader bufferedReader = null;
        StringBuilder retsult = new StringBuilder();
        try {
            InputStream inputStream = context.getAssets().open(jsonFilePath);
            if (inputStream == null)
                return null;
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String infoStrLine;
            while ((infoStrLine = bufferedReader.readLine()) != null) {
                retsult.append(infoStrLine);
            }
        } catch (Exception e) {
            Logger.e(TAG, "fail to read json" + jsonFilePath, e);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                Logger.e(TAG, "fail to close bufferedReader", e);
            }
        }
        return retsult.toString();
    }

    public static class FxJsonFileInfo {

        public ArrayList<JsonFileInfo> getFxInfoList() {
            return fxInfoList;
        }

        private ArrayList<JsonFileInfo> fxInfoList;

        public static class JsonFileInfo {
            public String getName() {
                return name;
            }

            private String name;//素材名字

            public String getFxPackageId() {
                return fxPackageId;
            }

            private String fxPackageId;//素材包Id

            public String getFxFileName() {
                return fxFileName;
            }

            private String fxFileName;//素材特效包文件名

            public String getFxLicFileName() {
                return fxLicFileName;
            }

            private String fxLicFileName;//素材特效包授权文件名

            public String getImageName() {
                return imageName;
            }

            private String imageName;//素材封面

            public String getFitRatio() {
                return fitRatio;
            }

            private String fitRatio;//适配比例，参考NvAsset的定义
        }
    }
}
