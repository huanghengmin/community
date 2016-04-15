package com.hzih.community.domain;

import java.util.Set;

/**
 * Created by Administrator on 16-3-9.
 */
public class CommunityRoom {
    private long id;
    private int room;
    private String qrCode;
    private CommunityDoorplate communityDoorplate;
    private Set<Tenant> tenants;

    public CommunityRoom() {
    }

    public CommunityRoom(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public CommunityDoorplate getCommunityDoorplate() {
        return communityDoorplate;
    }

    public void setCommunityDoorplate(CommunityDoorplate communityDoorplate) {
        this.communityDoorplate = communityDoorplate;
    }

    public Set<Tenant> getTenants() {
        return tenants;
    }

    public void setTenants(Set<Tenant> tenants) {
        this.tenants = tenants;
    }
}
