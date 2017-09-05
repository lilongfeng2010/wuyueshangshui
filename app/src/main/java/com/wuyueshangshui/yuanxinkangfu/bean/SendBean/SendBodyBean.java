package com.wuyueshangshui.yuanxinkangfu.bean.SendBean;

import com.google.gson.JsonObject;
import com.wuyueshangshui.yuanxinkangfu.bean.WXUserBean;

import org.json.JSONObject;

/**
 * Created by lilfi on 2017/8/21.
 */

public class SendBodyBean {
    private String user_id;
    private String page;
    private String code;
    private String mobile;
    private String pass;
    private String account;
    private String unionid;
    private WXUserBean wx_user;
    private String  appoint_id;
    private int type;
    private String job;
    private String name;
    private String id_card_no;
    private String password;
    private int family_id;
    private String family_name;
    private String community_name;
    private String address;
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public int getFamily_id() {
        return family_id;
    }

    public void setFamily_id(int family_id) {
        this.family_id = family_id;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId_card_no() {
        return id_card_no;
    }

    public void setId_card_no(String id_card_no) {
        this.id_card_no = id_card_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAppoint_id() {
        return appoint_id;
    }

    public void setAppoint_id(String appoint_id) {
        this.appoint_id = appoint_id;
    }

    public WXUserBean getWx_user() {
        return wx_user;
    }

    public void setWx_user(WXUserBean wx_user) {
        this.wx_user = wx_user;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "SendBodyBean{" +
                "user_id='" + user_id + '\'' +
                ", page='" + page + '\'' +
                ", code='" + code + '\'' +
                ", mobile='" + mobile + '\'' +
                ", pass='" + pass + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}
