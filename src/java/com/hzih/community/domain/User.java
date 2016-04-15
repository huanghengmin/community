package com.hzih.community.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 16-1-25.
 */
public class User implements Serializable{
    private Long id; //编号
    private String idCard;//身份证
    private String name;//姓名
    private String number;//编号
    private String password;//登陆密码
    private String phone;//注册手机
    private Date register_time;//注册时间
    private Date modify_time;//更新时间
    private int status;
    private Community community;

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public User(Long id) {
        this.id = id;
    }

    public User(String phone) {
        this.phone = phone;
    }

    public User(String password, String phone) {
        this.password = password;
        this.phone = phone;
    }

    public User() {
    }

   public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getRegister_time() {
        return register_time;
    }

    public void setRegister_time(Date register_time) {
        this.register_time = register_time;
    }

    public Date getModify_time() {
        return modify_time;
    }

    public void setModify_time(Date modify_time) {
        this.modify_time = modify_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
