package zippler.cn.xs.gson;

import java.util.List;

/**
 * Created by Zipple on 2018/5/22.
 */

public class VideoGson {

    /**
     * id : 6
     * url : E:\rc\xsserver\video\邹博_开发文档.docx
     * poster : null
     * description : 今天的太阳比昨天更大了，真希望
     * comments : []
     * likes : 0
     * deployer : {"id":5,"nickname":"李欣雨","password":"root","avatar":"avatar.jpg","favideos":[],"sex":0,"birthday":null,"school":"CSU","registerTime":"2018-05-20T14:32:52.000+0800","fansId":null,"comment":[]}
     * favUsers : []
     * deployTime : 2018-05-20T14:35:59.000+0800
     */

    private int id;
    private String url;
    private Object poster;
    private String description;
    private int likes;
    private DeployerBean deployer;
    private String deployTime;
    private List<?> comments;
    private List<?> favUsers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getPoster() {
        return poster;
    }

    public void setPoster(Object poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public DeployerBean getDeployer() {
        return deployer;
    }

    public void setDeployer(DeployerBean deployer) {
        this.deployer = deployer;
    }

    public String getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(String deployTime) {
        this.deployTime = deployTime;
    }

    public List<?> getComments() {
        return comments;
    }

    public void setComments(List<?> comments) {
        this.comments = comments;
    }

    public List<?> getFavUsers() {
        return favUsers;
    }

    public void setFavUsers(List<?> favUsers) {
        this.favUsers = favUsers;
    }

    public static class DeployerBean {
        /**
         * id : 5
         * nickname : 李欣雨
         * password : root
         * avatar : avatar.jpg
         * favideos : []
         * sex : 0
         * birthday : null
         * school : CSU
         * registerTime : 2018-05-20T14:32:52.000+0800
         * fansId : null
         * comment : []
         */

        private int id;
        private String nickname;
        private String password;
        private String avatar;
        private int sex;
        private Object birthday;
        private String school;
        private String registerTime;
        private Object fansId;
        private List<?> favideos;
        private List<?> comment;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public Object getBirthday() {
            return birthday;
        }

        public void setBirthday(Object birthday) {
            this.birthday = birthday;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(String registerTime) {
            this.registerTime = registerTime;
        }

        public Object getFansId() {
            return fansId;
        }

        public void setFansId(Object fansId) {
            this.fansId = fansId;
        }

        public List<?> getFavideos() {
            return favideos;
        }

        public void setFavideos(List<?> favideos) {
            this.favideos = favideos;
        }

        public List<?> getComment() {
            return comment;
        }

        public void setComment(List<?> comment) {
            this.comment = comment;
        }
    }
}
