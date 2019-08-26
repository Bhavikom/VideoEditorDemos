package zippler.cn.xs.gson;

import java.util.List;

/**
 * Created by Zipple on 2018/5/22.
 */

public class VideoGuideGson {


    /**
     * status : 1
     * info : 上传成功
     * data : [{"video1":"speed1"},{"video2":"speed2"}]
     */

    private String status;
    private String info;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * video1 : speed1
         * video2 : speed2
         */

        private List<String> video;

        public List<String> getVideo() {
            return video;
        }

        public void setVideo(List<String> video) {
            this.video = video;
        }
    }
}
