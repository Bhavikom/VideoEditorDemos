package zippler.cn.xs.entity;

import java.io.Serializable;

/**
 * Created by Zipple on 2018/5/5.
 * The video will return from the cloud
 */
public class Video implements Serializable {
    private int id;
    private User user;
    private String name;
    private String url;
    private String localStorageUrl;
    private String desc;
    private String deployed;
    private int favorite;
    private String poster;
    private String length;//the total time of the video

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDeployed() {
        return deployed;
    }

    public void setDeployed(String deployed) {
        this.deployed = deployed;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = formatTime(length);
    }

    public static String formatTime(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        return strMinute + ":" + strSecond;
    }

    public String getLocalStorageUrl() {
        return localStorageUrl;
    }

    public void setLocalStorageUrl(String localStorageUrl) {
        this.localStorageUrl = localStorageUrl;
    }
}
