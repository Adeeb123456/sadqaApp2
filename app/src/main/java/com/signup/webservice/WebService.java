package com.signup.webservice;

import com.signup.model.GetLogin;
import com.signup.model.login.LoginModel;
import com.signup.model.registermodel.SignUpModel;
import com.signup.utils.AppConst;

import java.util.Map;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface WebService {


    @GET("login")
    Call<LoginModel> loginUser(@QueryMap Map<String, String> options);





    @FormUrlEncoded
    @POST(AppConst.Update_Profile)
    Call<LoginModel>postUpdateprofile(@Field(AppConst.U_FullName) String name,
                                      @Field(AppConst.U_Email) String email,
                                      @Field(AppConst.U_Password) String password,
                                      @Field(AppConst.U_DeviceToken) String token,
                                      @Field(AppConst.U_DeviceType) String devictype,
                                      @Field(AppConst.U_Image) String image);

    @FormUrlEncoded
    @POST(AppConst.KeySignup)
    Call<SignUpModel>postSignupUser(@Field(AppConst.U_FullName) String name,
                                    @Field(AppConst.U_Email) String email,
                                    @Field(AppConst.U_Password) String password,
                                    @Field(AppConst.U_DeviceToken) String token,
                                    @Field(AppConst.U_DeviceType) String devictype,
                                    @Field(AppConst.U_Image) String image);


    @GET("?")
    Call<GetLogin> getLogIn(@QueryMap Map<String, String> options);
    @GET("?" + AppConst.serviceType + "=" + AppConst.GET_CUSTOMER_DETAIL)
    Call<GetLogin> getUserDetail(@Query(AppConst.customerId) String id);
//    @GET("?")
//    Call<GetVideos> getVideos(@QueryMap Map<String, String> options);

//    @GET("?" + AppConst.serviceType + "=" + AppConst.GET_FAQ)
//    Call<GetFaqs> getFaqs(@Query(AppConst.appLanguage) String lang);

//    @GET("?" + AppConst.serviceType + "=" + AppConst.GET_ABOUT_COMPANY)
//    Call<GetAboutUs> getAboutComp(@Query(AppConst.appLanguage) String lang, @Query(AppConst.deviceToken) String deviceToken, @Query(AppConst.deviceType) String deviceType, @Query(AppConst.customerId) String customerId);

//    @GET("?")
//    Call<GetStatus> postFeedBack(@QueryMap Map<String, String> options);

}
