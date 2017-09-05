package com.wuyueshangshui.yuanxinkangfu.bean;

import java.util.List;

/**
 * Created by lilfi on 2017/8/24.
 */

public class WXUserBean {

    /**
     * openid : okzuD1Z_HVPphnUBPz6_x5UHlkMw
     * nickname : 野比大雄
     * sex : 1
     * language : zh_CN
     * city : 海淀
     * province : 北京
     * country : 中国
     * headimgurl : http://wx.qlogo.cn/mmopen/Q3auHgzwzM6Xe5Jexcr8QTia7mNCoABRtI7hQLKI9uSZowNKt1y7NP8bvPXZe5ibFmant9mPPoXUWYIMdBTyOficahxT8WozA47OzJdGfLx25Q/0
     * privilege : []
     * unionid : otJFEv6ScsvUjVgz1u4P4YFFBlRE
     */

    private String openid;
    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<?> privilege;
    /**
     * id : 5
     * user_id : 10000430
     * info : {"city":"\u6d77\u6dc0","country":"\u4e2d\u56fd","headimgurl":"http:\/\/wx.qlogo.cn\/mmopen\/Q3auHgzwzM6Xe5Jexcr8QTia7mNCoABRtI7hQLKI9uSZowNKt1y7NP8bvPXZe5ibFmant9mPPoXUWYIMdBTyOficahxT8WozA47OzJdGfLx25Q\/0","language":"zh_CN","nickname":"\u91ce\u6bd4\u5927\u96c4","openid":"okzuD1Z_HVPphnUBPz6_x5UHlkMw","privilege":[],"province":"\u5317\u4eac","sex":1,"unionid":"otJFEv6ScsvUjVgz1u4P4YFFBlRE"}
     * created : 1503558883
     */

    private int id;
    private int user_id;
    private String info;
    private int created;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public List<?> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<?> privilege) {
        this.privilege = privilege;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }
}
