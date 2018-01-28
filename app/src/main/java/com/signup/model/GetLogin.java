package com.signup.model;

import com.google.gson.annotations.SerializedName;
import com.signup.utils.AppConst;


/**
 * Created by USER3 on 2/5/2017.
 */

public class GetLogin {


    @SerializedName(AppConst.success)
    public boolean state;

    @SerializedName(AppConst.content)
    public User content;

  //  @SerializedName(AppConst.error)
   // public StatusCodes error;
}
