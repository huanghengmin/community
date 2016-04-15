package com.hzih.community.domain;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by Administrator on 16-2-18.
 */
public class CommunityDoorplate implements Serializable {
    private long id;
    private CommunityBuildUnit communityBuildUnit;
    private int doorplate;
    private int status;
    private String qrCode;
    private Set<LardLord> lardLords;
    private Set<CommunityRoom> communityRoomSet;

    public CommunityDoorplate() {
    }

    public CommunityDoorplate(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CommunityBuildUnit getCommunityBuildUnit() {
        return communityBuildUnit;
    }

    public void setCommunityBuildUnit(CommunityBuildUnit communityBuildUnit) {
        this.communityBuildUnit = communityBuildUnit;
    }

    public int getDoorplate() {
        return doorplate;
    }

    public void setDoorplate(int doorplate) {
        this.doorplate = doorplate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<CommunityRoom> getCommunityRoomSet() {
        return communityRoomSet;
    }

    public void setCommunityRoomSet(Set<CommunityRoom> communityRoomSet) {
        this.communityRoomSet = communityRoomSet;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Set<LardLord> getLardLords() {
        return lardLords;
    }

    public void setLardLords(Set<LardLord> lardLords) {
        this.lardLords = lardLords;
    }
}
