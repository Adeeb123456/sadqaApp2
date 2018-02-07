package com.geniteam.SadqaApp.frags;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.geniteam.SadqaApp.AppBase;
import com.geniteam.SadqaApp.R;
import com.geniteam.SadqaApp.base.DrawerItemBaseFragment;
import com.geniteam.SadqaApp.databinding.StrartFragBinding;
import com.geniteam.SadqaApp.model.login.AppUser;
import com.geniteam.SadqaApp.webservice.FireBaseTransaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by adeeb on 2/4/2018.
 */

public class StartFragment extends DrawerItemBaseFragment implements View.OnClickListener {
    StrartFragBinding strartFragBinding;



    @Override
    public String getTagText() {
        return null;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void fbLoginWithCustomButton() {



                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(AppBase.mCallbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                // App code
                                // startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                              //  Toast.makeText(getContext(), "login success", Toast.LENGTH_LONG).show();
                                Log.d("debug", "facebook:onSuccess:" + loginResult);

                                  AppUser appUser=new AppUser();
                                //By Profile Class
                                Profile profile = Profile.getCurrentProfile();
                                if (profile != null) {
                                    String  facebook_id=profile.getId();
                                    String   f_name=profile.getFirstName();
                                    String    m_name=profile.getMiddleName();
                                    String   l_name=profile.getLastName();
                                    String  full_name=profile.getName();
                                    String   profile_image=profile.getProfilePictureUri(400, 400).toString();

                                    appUser.setUFullName(full_name);
                                    appUser.setUImageURL(profile_image);

                                }
                                //Toast.makeText(FacebookLogin.this,"Wait...",Toast.LENGTH_SHORT).show();
                                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(JSONObject object, GraphResponse response) {
                                                try {
                                                    Log.d("debug","json"+ object.toString());
                                                    String  email_id=object.getString("email");
                                                    String gender=object.getString("gender");
                                                    String profile_name=object.getString("name");
                                                    Log.d("debug","name "+profile_name);
                                                   // String phone= object.getString("phone");

                                                   // Log.d("debug","phone "+phone);
                                                    long fb_id=object.getLong("id"); //use this for logout

                                                } catch (JSONException e) {
                                                    Log.d("debug"," err"+e);
                                                    // TODO Auto-generated catch block
                                                    //  e.printStackTrace();
                                                }

                                            }

                                        });
                                Bundle bundle = new Bundle();
                                bundle.putString(
                                        "fields",
                                        "id,name,link,email,gender,last_name,first_name,locale,timezone,updated_time,verified"
                                );
                                request.setParameters(bundle);
                                request.executeAsync();

                                handleFacebookAccessToken(loginResult.getAccessToken());
                            }

                            @Override
                            public void onCancel() {
                                // App code
                                Toast.makeText(getContext(), "login fail ", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                                Toast.makeText(getContext(), "login fail "+exception, Toast.LENGTH_LONG).show();

                            }
                        });
            }





    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("debug", "handleFacebookAccessToken:" + token);
try{


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        AppBase.mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("debug", "signInWithCredential:success");
                            FirebaseUser user = AppBase.mFirebaseAuth.getCurrentUser();
                            FireBaseTransaction.fetchFireBaseUser(getContext(), user.getUid(), new FireBaseTransaction.FetchUserCallback() {
                                @Override
                                public void onFireBaseResponse(AppUser appUser, boolean isSuccess) {
                                    hostActivityInterface.setUpDrawerUi(appUser);

                                    hostActivityInterface.showDrawerItemFragment(new HomeFragment());
                                }

                                @Override
                                public void onFailure(AppUser appUser, boolean isSuccess, String errorMsg) {
Toast.makeText(getContext(),"Fail to Registered",Toast.LENGTH_LONG).show();
                                }
                            });
                            writeNewUser(user.getUid(),user.getEmail(),user.getDisplayName(),user.getPhotoUrl()+"");
                            FireBaseTransaction.fetchFireBaseUser(getContext(), user.getUid(), new FireBaseTransaction.FetchUserCallback() {
                                @Override
                                public void onFireBaseResponse(AppUser appUser, boolean isSuccess) {
                                    hostActivityInterface.setUpDrawerUi(appUser);

                                    hostActivityInterface.showDrawerItemFragment(new HomeFragment());
                                }

                                @Override
                                public void onFailure(AppUser appUser, boolean isSuccess, String errorMsg) {
                                    Toast.makeText(getContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("deug", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

}catch (Exception e){
    e.printStackTrace();
}
    }

    public void writeNewUser(String uid,String email, String name,String picUrl){
      //  User user=new User(name,email,picUrl);
        AppUser appUser=new AppUser();
        appUser.setUFullName(name);
        appUser.setuPhone("");
        appUser.setUPassword("");
        appUser.setUImageURL(picUrl);

        FireBaseTransaction.writNewUser(uid,appUser);

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(strartFragBinding==null){
            strartFragBinding= DataBindingUtil.inflate(inflater, R.layout.strart_frag,container,false);
strartFragBinding.createAccount.setOnClickListener(this);
strartFragBinding.loginwithfb.setOnClickListener(this);
strartFragBinding.login.setOnClickListener(this);
        }

        return strartFragBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onClick(View v) {
        if(v==strartFragBinding.createAccount){
hostActivityInterface.addFragment(new SignUpFrag(),true);
        }else if(v==strartFragBinding.loginwithfb){
fbLoginWithCustomButton();
        }else if(v==strartFragBinding.login){
            hostActivityInterface.showDrawerItemFragment(new LoginFragment());
        }
    }


}
