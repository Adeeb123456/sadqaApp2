
package com.geniteam.SadqaApp.model.registermodel;

import java.util.HashMap;
import java.util.Map;

public class AppUser {

    private Integer UserID;
    private String U_FullName;
    private String U_Email;
    private String U_Password;
    private Object U_ImageURL;
    private String U_DeviceToken;
    private String U_DeviceType;
    private String U_CreatedDate;
    private Boolean U_isActive;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getUserID() {
        return UserID;
    }

    public void setUserID(Integer userID) {
        this.UserID = userID;
    }

    public String getUFullName() {
        return U_FullName;
    }

    public void setUFullName(String uFullName) {
        this.U_FullName = uFullName;
    }

    public String getUEmail() {
        return U_Email;
    }

    public void setUEmail(String uEmail) {
        this.U_Email = uEmail;
    }

    public String getUPassword() {
        return U_Password;
    }

    public void setUPassword(String uPassword) {
        this.U_Password = uPassword;
    }

    public Object getUImageURL() {
        return U_ImageURL;
    }

    public void setUImageURL(Object uImageURL) {
        this.U_ImageURL = uImageURL;
    }

    public String getUDeviceToken() {
        return U_DeviceToken;
    }

    public void setUDeviceToken(String uDeviceToken) {
        this.U_DeviceToken = uDeviceToken;
    }

    public String getUDeviceType() {
        return U_DeviceType;
    }

    public void setUDeviceType(String uDeviceType) {
        this.U_DeviceType = uDeviceType;
    }

    public String getUCreatedDate() {
        return U_CreatedDate;
    }

    public void setUCreatedDate(String uCreatedDate) {
        this.U_CreatedDate = uCreatedDate;
    }

    public Boolean getUIsActive() {
        return U_isActive;
    }

    public void setUIsActive(Boolean uIsActive) {
        this.U_isActive = uIsActive;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
