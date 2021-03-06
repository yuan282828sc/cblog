package com.zcw.cblog.user.vo;

/**
 *
 * @Description TODO:用户更新用户信息
 * @Author YuYu
 * @Date 2020-01-28 14:34
 * @Version 1.0
 */
public class UserInfoVo {
    private String nickname;
    private String sex;
    private String birthday;
    private String location;
    private String signature;
    private String Mobile;

    @Override
    public String toString() {
        return "UserInfoVo{" +
                "nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", location='" + location + '\'' +
                ", signature='" + signature + '\'' +
                ", Mobile='" + Mobile + '\'' +
                '}';
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
