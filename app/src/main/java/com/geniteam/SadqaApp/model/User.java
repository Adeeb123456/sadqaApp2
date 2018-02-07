package com.geniteam.SadqaApp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.geniteam.SadqaApp.utils.AppConst;


/**
 * Created by USER3 on 2/5/2017.
 */
public class User {

    @SerializedName(AppConst.idParam)
    @Expose
    private String id;

    @SerializedName(AppConst.email)
    @Expose
    private String email;

    @SerializedName(AppConst.firstName)
    @Expose
    private String firstname;

    @SerializedName(AppConst.gender)
    @Expose
    private int gender;

    @SerializedName(AppConst.phone)
    @Expose
    private String phone;

    @SerializedName(AppConst.createDate)
    @Expose
    private String createDate;

    @SerializedName(AppConst.modificationDate)
    @Expose
    private String modificationDate;

    @SerializedName(AppConst.status)
    @Expose
    private int status;

    @SerializedName(AppConst.totalFavorites)
    @Expose
    private int favCount;

    @SerializedName(AppConst.pushNotification)
    @Expose
    private int pushNotification;

    @SerializedName(AppConst.activationDate)
    @Expose
    private String activationDate;

    @SerializedName(AppConst.sinceDate)
    @Expose
    private String sinceDate;

    @SerializedName(AppConst.image)
    @Expose
    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPushNotification() {
        return pushNotification;
    }

    public void setPushNotification(int pushNotification) {
        this.pushNotification = pushNotification;
    }

    public String getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    public String getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(String sinceDate) {
        this.sinceDate = sinceDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getFavCount() {
        return favCount;
    }

    public void setFavCount(int favCount) {
        this.favCount = favCount;
    }
}