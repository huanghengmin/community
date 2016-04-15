package com.hzih.community.domain;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by Administrator on 16-2-18.
 * 楼栋
 */
public class CommunityBuild implements Serializable {
    private long id;//编号
    private int value;//楼栋
    private Community community;
    private String principal;//负责人
    private String principal_phone;//负责人电话
    private Set<CommunityBuildUnit> communityBuildUnits;

    public CommunityBuild(int l) {
        this.id = l;
    }

    public CommunityBuild() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public Set<CommunityBuildUnit> getCommunityBuildUnits() {
        return communityBuildUnits;
    }

    public void setCommunityBuildUnits(Set<CommunityBuildUnit> communityBuildUnits) {
        this.communityBuildUnits = communityBuildUnits;
    }
}
