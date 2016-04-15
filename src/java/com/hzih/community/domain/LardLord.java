package com.hzih.community.domain;

import java.sql.Blob;
import java.util.Date;

/**
 * Created by Administrator on 16-2-25.
 */
public class LardLord {
    private long id;
    private String name ;
    private String sex ;
    private String idCard;
    private String mz ;
    private String birth ;
    private String sign;
    private String address;
    private String DN;
    private String validity;
    private String phone;
    private String description;
    private Blob bytes;
    private Blob cardPicBefore;
    private Blob cardPicAfter;
    private int attention;
    private Date initDate;
    private Date lastDate;
    private int status;
    private int ocr;
    private String xzz;

    private CommunityDoorplate communityDoorplate;

    public LardLord() {
    }

    public LardLord(long id) {
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setBytes(Blob bytes) {
        this.bytes = bytes;
    }

    public Blob getBytes() {
        return bytes;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDN() {
        return DN;
    }

    public void setDN(String DN) {
        this.DN = DN;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CommunityDoorplate getCommunityDoorplate() {
        return communityDoorplate;
    }

    public void setCommunityDoorplate(CommunityDoorplate communityDoorplate) {
        this.communityDoorplate = communityDoorplate;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public Blob getCardPicBefore() {
        return cardPicBefore;
    }

    public void setCardPicBefore(Blob cardPicBefore) {
        this.cardPicBefore = cardPicBefore;
    }

    public Blob getCardPicAfter() {
        return cardPicAfter;
    }

    public void setCardPicAfter(Blob cardPicAfter) {
        this.cardPicAfter = cardPicAfter;
    }

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOcr() {
        return ocr;
    }

    public void setOcr(int ocr) {
        this.ocr = ocr;
    }

    public String getXzz() {
        return xzz;
    }

    public void setXzz(String xzz) {
        this.xzz = xzz;
    }

}
