
package com.signup.model.login;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppUser {

    @SerializedName("Favourites_Count")
    @Expose
    private Integer favouritesCount;
    @SerializedName("UserID")
    @Expose
    private Integer userID;
    @SerializedName("U_FullName")
    @Expose
    private String uFullName;
    @SerializedName("U_Email")
    @Expose
    private String uEmail;
    @SerializedName("U_Password")
    @Expose
    private String uPassword;
    @SerializedName("U_ImageURL")
    @Expose
    private String uImageURL;
    @SerializedName("U_DeviceToken")
    @Expose
    private String uDeviceToken;
    @SerializedName("U_DeviceType")
    @Expose
    private String uDeviceType;
    @SerializedName("U_CreatedDate")
    @Expose
    private String uCreatedDate;
    @SerializedName("U_isActive")
    @Expose
    private Boolean uIsActive;

    public Integer getFavouritesCount() {
        return favouritesCount;
    }

    public void setFavouritesCount(Integer favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUFullName() {
        return uFullName;
    }

    public void setUFullName(String uFullName) {
        this.uFullName = uFullName;
    }

    public String getUEmail() {
        return uEmail;
    }

    public void setUEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getUPassword() {
        return uPassword;
    }

    public void setUPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public String getUImageURL() {
        return uImageURL;
    }

    public void setUImageURL(String uImageURL) {
        this.uImageURL = uImageURL;
    }

    public String getUDeviceToken() {
        return uDeviceToken;
    }

    public void setUDeviceToken(String uDeviceToken) {
        this.uDeviceToken = uDeviceToken;
    }

    public String getUDeviceType() {
        return uDeviceType;
    }

    public void setUDeviceType(String uDeviceType) {
        this.uDeviceType = uDeviceType;
    }

    public String getUCreatedDate() {
        return uCreatedDate;
    }

    public void setUCreatedDate(String uCreatedDate) {
        this.uCreatedDate = uCreatedDate;
    }

    public Boolean getUIsActive() {
        return uIsActive;
    }

    public void setUIsActive(Boolean uIsActive) {
        this.uIsActive = uIsActive;
    }

}
