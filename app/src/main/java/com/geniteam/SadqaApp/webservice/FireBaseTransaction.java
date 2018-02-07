package com.geniteam.SadqaApp.webservice;

import android.content.Context;

import com.geniteam.SadqaApp.AppBase;
import com.geniteam.SadqaApp.model.login.AppUser;
import com.geniteam.SadqaApp.utils.AppConst;
import com.geniteam.SadqaApp.utils.AppPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

/**
 * Created by adeeb on 2/5/2018.
 */

public class FireBaseTransaction {
   public interface FetchUserCallback{
        public void onFireBaseResponse(AppUser appUser,boolean isSuccess);
       public void onFailure(AppUser appUser,boolean isSuccess,String errorMsg);

   }
    public static void  fetchFireBaseUser(final Context context, String uuid, final FetchUserCallback response){
        AppBase.mFireBaseDbReference.child(AppConst.fireBaseUserDbReference).child(uuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(context,"user registered successfully",Toast.LENGTH_LONG).show();
                AppUser  appUser=dataSnapshot.getValue(AppUser.class);
                Gson gson=new Gson();
                String json = gson.toJson(appUser);
                AppPref.putValueByKey(AppConst.keyUser, json);
                AppPref.saveUserModel(appUser);
                response.onFireBaseResponse(appUser,true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                response.onFailure(null,false,databaseError.getMessage());
            }
        });
    }


    public static void writNewUser(String uid,AppUser appUser){
        AppBase.mFireBaseDbReference.child(AppConst.fireBaseUserDbReference).child(uid).setValue(appUser);

    }
}
