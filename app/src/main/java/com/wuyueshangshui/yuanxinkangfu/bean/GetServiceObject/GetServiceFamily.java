package com.wuyueshangshui.yuanxinkangfu.bean.GetServiceObject;

import java.util.Map;

/**
 * Created by lilfi on 2017/8/29.
 */

public class GetServiceFamily {


    /**
     * id : 1000080
     * user_id : 10000081
     * family_name : xiao.幸福
     * vip_level : 1
     * community_id : 1
     * community_name : 测试小区
     * address : 100号楼21室
     * vip_end : 1483977600
     * created : 1473315696
     */

    private int id;
    private int user_id;
    private String family_name;
    private int vip_level;
    private int community_id;
    private String community_name;
    private String address;
    private int vip_end;
    private String created;
    private Map<String,GetServiceUserBean> users;

    public Map<String, GetServiceUserBean> getUsers() {
        return users;
    }

    public void setUsers(Map<String, GetServiceUserBean> users) {
        this.users = users;
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

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public int getVip_level() {
        return vip_level;
    }

    public void setVip_level(int vip_level) {
        this.vip_level = vip_level;
    }

    public int getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(int community_id) {
        this.community_id = community_id;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getVip_end() {
        return vip_end;
    }

    public void setVip_end(int vip_end) {
        this.vip_end = vip_end;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
