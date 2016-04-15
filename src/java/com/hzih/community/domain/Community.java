package com.hzih.community.domain;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Set;

/**
 * Created by Administrator on 16-2-18.
 * 社区
 */
public class Community implements Serializable {
    private long id; //小区id
    private String name; //小区名称
//    private GovCode govCode;
    private Region region;
//    private String ssxq; //所属县区
//    private String ssxqname; //所属县区
    private String number;//小区编号
    private String address;//小区地址
    private String principal;//负责人
    private String principal_phone;//负责人电话
    private Blob logo;//小区logo

    private Set<CommunityBuild> communityBuilds;

    public Community() {
    }

 /*   public GovCode getGovCode() {
        return govCode;
    }

    public void setGovCode(GovCode govCode) {
        this.govCode = govCode;
    }*/

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Community(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<CommunityBuild> getCommunityBuilds() {
        return communityBuilds;
    }

    public void setCommunityBuilds(Set<CommunityBuild> communityBuilds) {
        this.communityBuilds = communityBuilds;
    }

    public Blob getLogo() {
        return logo;
    }

    public void setLogo(Blob logo) {
        this.logo = logo;
    }

   /* public String getSsxq() {
        return ssxq;
    }

    public void setSsxq(String ssxq) {
        this.ssxq = ssxq;
    }*/

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

   /* public String getSsxqname() {
        return ssxqname;
    }

    public void setSsxqname(String ssxqname) {
        this.ssxqname = ssxqname;
    }*/
}
