package com.meishe.sdkdemo.capturescene.data;

import java.util.List;

import static com.meishe.sdkdemo.capturescene.data.Constants.CAPTURE_SCENE_LOCAL;

/**
 * Created by CaoZhiChao on 2019/1/3 15:21
 */
public class CaptureSceneOnlineData {

    /**
     * errNo : 0
     * hasNext : false
     * list : [{"id":"8FB5A4C7-BAFC-4FCD-9994-F496A78F47C3","category":1,"name":"scene1","desc":"","tags":"","version":1,"minAppVersion":"","packageUrl":"https://meishesdk.meishe-app.com/material/capturescene/8FB5A4C7-BAFC-4FCD-9994-F496A78F47C3.capturescene","packageSize":5200000,"coverUrl":"https://meishesdk.meishe-app.com/material/capturescene/8FB5A4C7-BAFC-4FCD-9994-F496A78F47C3.png","supportedAspectRatio":4},{"id":"897F4258-74C0-4F89-884E-3E6C07E3EE0E","category":1,"name":"scene2","desc":"","tags":"","version":1,"minAppVersion":"","packageUrl":"https://meishesdk.meishe-app.com/material/capturescene/897F4258-74C0-4F89-884E-3E6C07E3EE0E.capturescene","packageSize":5500000,"coverUrl":"https://meishesdk.meishe-app.com/material/capturescene/897F4258-74C0-4F89-884E-3E6C07E3EE0E.png","supportedAspectRatio":4}]
     */

    private int errNo;
    private boolean hasNext;
    private List<CaptureSceneDetails> list;

    public int getErrNo() {
        return errNo;
    }

    public void setErrNo(int errNo) {
        this.errNo = errNo;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<CaptureSceneDetails> getList() {
        return list;
    }

    public void setList(List<CaptureSceneDetails> list) {
        this.list = list;
    }

    public static class CaptureSceneDetails {
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof CaptureSceneDetails) {
                CaptureSceneDetails captureSceneDetails = (CaptureSceneDetails) obj;
                return this.id.equals(captureSceneDetails.getId());
            }
            return super.equals(obj);
        }

        /**
         * id : 8FB5A4C7-BAFC-4FCD-9994-F496A78F47C3
         * category : 1
         * name : scene1
         * desc :
         * tags :
         * version : 1
         * minAppVersion :
         * packageUrl : https://meishesdk.meishe-app.com/material/capturescene/8FB5A4C7-BAFC-4FCD-9994-F496A78F47C3.capturescene
         * packageSize : 5200000
         * coverUrl : https://meishesdk.meishe-app.com/material/capturescene/8FB5A4C7-BAFC-4FCD-9994-F496A78F47C3.png
         * supportedAspectRatio : 4
         */


        private String id;
        private int category;
        private String name;
        private String desc;
        private String tags;
        private int version;
        private String minAppVersion;
        private String packageUrl;
        private int packageSize;
        private String coverUrl;
        private int supportedAspectRatio;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        private int type = CAPTURE_SCENE_LOCAL;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getMinAppVersion() {
            return minAppVersion;
        }

        public void setMinAppVersion(String minAppVersion) {
            this.minAppVersion = minAppVersion;
        }

        public String getPackageUrl() {
            return packageUrl;
        }

        public void setPackageUrl(String packageUrl) {
            this.packageUrl = packageUrl;
        }

        public int getPackageSize() {
            return packageSize;
        }

        public void setPackageSize(int packageSize) {
            this.packageSize = packageSize;
        }

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public int getSupportedAspectRatio() {
            return supportedAspectRatio;
        }

        public void setSupportedAspectRatio(int supportedAspectRatio) {
            this.supportedAspectRatio = supportedAspectRatio;
        }
    }
}
