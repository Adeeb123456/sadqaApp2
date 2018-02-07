
package com.geniteam.SadqaApp.model.registermodel;

import java.util.HashMap;
import java.util.Map;

public class SignUpModel {

    private Boolean success;
    private AppUser App_User;
    private Error error;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public AppUser getAppUser() {
        return App_User;
    }

    public void setAppUser(AppUser appUser) {
        this.App_User = appUser;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

}
