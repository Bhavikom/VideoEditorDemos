package zippler.cn.xs.entity;

import java.sql.Timestamp;

/**
 * Created by Zipple on 2018/5/5.
 * User modal
 */

public class User {
    private String avatar;
    private int id;
    private String username;
    private String password;//secret
    private String introduction;
    private Timestamp registerTime;

    public User(){

    }

    public User(String username,String avatar,String introduction){
        this.username = username;
        this.introduction = introduction;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
