package com.sirui.basiclib.data.bean;

import org.json.JSONObject;

/**
 * 用户信息保存
 * Created by xiepc on 2016/11/18 15:53
 */

public class User {

    private String age;
    private String cartNo;
    private String createDate;
    private String height;
    private String patientId;
    private String patientMobile;
    private String realName;
    private String sex;
    private String weight;
    private String passKey;
    private String token;
    private String birthday;
    private String imNo;
    private String isVisitor;

    public User() {
    }

    public User(String realName, String sex, String cartNo, String patientMobile) {
        this.cartNo = cartNo;
        this.sex = sex;
        this.patientMobile = patientMobile;
        this.realName = realName;
    }

    public User(JSONObject obj){
        age = obj.optString("age");
        cartNo = obj.optString("cartNo");
        createDate = obj.optString("createDate");
        height = obj.optString("height");
        patientId = obj.optString("patientId");
        patientMobile = obj.optString("patientMobile");
        realName = obj.optString("realName");
        sex = obj.optString("sex");
        weight = obj.optString("weight");
        passKey = obj.optString("passKey");
        birthday = obj.optString("birthday");
        imNo = obj.optString("imNo");
        token = obj.optString("token");
    }

    public String getImNo() {
        return imNo;
    }

    public void setImNo(String imNo) {
        this.imNo = imNo;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCartNo() {
        return cartNo;
    }

    public void setCartNo(String cartNo) {
        this.cartNo = cartNo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientMobile() {
        return patientMobile;
    }

    public void setPatientMobile(String patientMobile) {
        this.patientMobile = patientMobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPassKey() {
        return passKey;
    }

    public void setPassKey(String passKey) {
        this.passKey = passKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return sex;
    }

    public String getIsVisitor() {
        return isVisitor;
    }

    public void setIsVisitor(String isVisitor) {
        this.isVisitor = isVisitor;
    }

    public boolean isVisitor() {
        return "Y".equals(isVisitor);
    }

    @Override
    public String toString() {
        return "User{" + "age='" + age + '\'' + ", cartNo='" + cartNo + '\'' + ", createDate='" + createDate + '\'' + ", height='" + height + '\'' + ", patientId='" + patientId + '\'' + ", patientMobile='" + patientMobile + '\'' + ", realName='" + realName + '\'' + ", sex='" + sex + '\'' + ", weight='" + weight + '\'' + ", passKey='" + passKey + '\'' + ", token='" + token + '\'' + ", birthday='" + birthday + '\'' + ", imNo='" + imNo + '\'' + '}';
    }
}
