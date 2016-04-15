package com.hzih.community.domain;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by Administrator on 16-2-18.
 * 单元
 */
public class CommunityBuildUnit implements Serializable {
    private long id; //编号
    private int value; //名称
    private CommunityBuild communityBuild;
    private String principal;//负责人
    private String principal_phone;//负责人电话
    private Set<CommunityDoorplate> communityDoorplates;

    public CommunityBuildUnit(int i) {
        this.id = i;
    }

    public CommunityBuildUnit() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public CommunityBuild getCommunityBuild() {
        return communityBuild;
    }

    public void setCommunityBuild(CommunityBuild communityBuild) {
        this.communityBuild = communityBuild;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPrincipal_phone() {
        return principal_phone;
    }

    public void setPrincipal_phone(String principal_phone) {
        this.principal_phone = principal_phone;
    }

    public Set<CommunityDoorplate> getCommunityDoorplates() {
        return communityDoorplates;
    }

    public void setCommunityDoorplates(Set<CommunityDoorplate> communityDoorplates) {
        this.communityDoorplates = communityDoorplates;
    }
}
