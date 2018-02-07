package com.geniteam.SadqaApp.frags;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.geniteam.SadqaApp.CameraActivity;
import com.geniteam.SadqaApp.R;
import com.geniteam.SadqaApp.camerautils.EditSavePhotoFragment;

import com.geniteam.SadqaApp.databinding.FragSignupBinding;
import com.geniteam.SadqaApp.model.login.AppUser;
import com.geniteam.SadqaApp.utils.AppPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.geniteam.SadqaApp.utils.AppConst;
import com.geniteam.SadqaApp.utils.ProfilePicDialog;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.geniteam.SadqaApp.base.BaseFragment;


import com.geniteam.SadqaApp.utils.CircleTransform;
import com.geniteam.SadqaApp.utils.CommonUtils;
import com.geniteam.SadqaApp.utils.HideShowKeyPad;

import static com.geniteam.SadqaApp.frags.ProfileFrag.camPermission;


public class SignUpFrag extends BaseFragment implements View.OnClickListener, ProfilePicDialog.onProfilePicResponse {

    private FragSignupBinding binding;
    private String encodeImage = "";
    public Bitmap picBitmap;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReferenceUsers;
    Uri selectedImageUri;
    StorageReference storageReferencePicture,imageRef;
    UploadTask uploadTask;

    ProgressDialog progressDialog;
    @Override
    public String getTagText() {
        return "SignUpFrag";
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.frag_signup, container, false);
            ((AppCompatActivity) getActivity()).setSupportActionBar(binding.include.toolbar);
            binding.include.toolbar.setNavigationIcon(R.drawable.ic_back);

            binding.include.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hostActivityInterface.popBackStack();
                }
            });
        }



        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //setAllowReturnTransitionOverlap(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvLogin.setText(Html.fromHtml(getString(R.string.have_account), Html.FROM_HTML_MODE_LEGACY));
        } else {
            binding.tvLogin.setText(Html.fromHtml(getString(R.string.have_account)));
        }

        binding.tvLogin.setOnClickListener(this);
        binding.btnSignUp.setOnClickListener(this);
        binding.ivPicture.setOnClickListener(this);

        SpannableString s1 = new SpannableString(getString(R.string.read_privacy));

        s1.setSpan(new ClickPP(), 5, 19,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s1.setSpan(new ClickTC(), 24, s1.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvTerms.setText(s1);

        binding.tvTerms.setMovementMethod(LinkMovementMethod.getInstance());
        new HideShowKeyPad().setupUI(binding.parent);

    initFireBase();
    }


    public void initFireBase(){

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReferenceUsers= FirebaseDatabase.getInstance().getReference(AppConst.fireBaseUserDbReference);
        storageReferencePicture= FirebaseStorage.getInstance().getReference();


    }

    public void creatUserAccount(final AppUser appUser){
        firebaseAuth.createUserWithEmailAndPassword(appUser.getUEmail(),appUser.getUPassword())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
if(selectedImageUri!=null){
    //reg user with profile pic
    addUserWithPictureToFireBaseDB(appUser);
}else {
  // reg user with out profile pic

    addUserTOFireBaseDB(appUser,getCurrentFireBaseUserID());
}
                        }else {
if(task.getException()instanceof FirebaseAuthUserCollisionException){
    Snackbar snackbar=Snackbar.make(binding.getRoot(),"User already exists ",Snackbar.LENGTH_LONG);
    snackbar.setAction("ok",null);
}else if(task.getException()instanceof FirebaseNetworkException){
    Snackbar snackbar=Snackbar.make(binding.getRoot(),"Network problem ",Snackbar.LENGTH_LONG);
    snackbar.setAction("ok",null);
}else {
    Toast.makeText(getContext()," "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
    Log.d("debug",task.getException().getMessage());
}
                        }
                    }
                });
    }


    public void addUserWithPictureToFireBaseDB(final AppUser user){
        imageRef=storageReferencePicture.child("/images"+selectedImageUri.getLastPathSegment());
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMax(100);
        progressDialog.setMessage("uploading.. ");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();

        //start uploading

        uploadTask=imageRef.putFile(selectedImageUri);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                //sets and increments value of progressbar
                progressDialog.incrementProgressBy((int) progress);
            }
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // Handle unsuccessful uploads
                Toast.makeText(getActivity(),"Error in uploading!",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d("debug","url "+downloadUrl);
                Toast.makeText(getActivity(),"Upload successful",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                AppUser userWithProfilePic=user;
                userWithProfilePic.setUImageURL(downloadUrl.toString());
                String uuid= getCurrentFireBaseUserID();
                addUserTOFireBaseDB(userWithProfilePic,uuid);

                //showing the uploaded image in ImageView using the download url
                Picasso.with(getActivity()).load(downloadUrl).into(binding.ivPicture);
            }
        });



    }


    public void addUserTOFireBaseDB(AppUser user,String uuid){
        try{
            databaseReferenceUsers.child(uuid).setValue(user);
            databaseReferenceUsers.child(uuid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Toast.makeText(getContext(),"user registered successfully",Toast.LENGTH_LONG).show();
                    AppUser  appUser=dataSnapshot.getValue(AppUser.class);
                      Gson gson=new Gson();
                    String json = gson.toJson(appUser);
                     AppPref.putValueByKey(AppConst.keyUser, json);
                      AppPref.saveUserModel(appUser);
                    hostActivityInterface.setUpDrawerUi(appUser);
                    hostActivityInterface.showDrawerItemFragment(new HomeFragment());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(),"Fail to registered",Toast.LENGTH_LONG).show();

                }
            });
            binding.setCanShowLoader(false);

        }catch (Exception e){
            Toast.makeText(getContext(),"Fail to registered",Toast.LENGTH_LONG).show();

        }


    }


    public String getCurrentFireBaseUserID(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId=null;
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            Log.d("debug","name "+user.getDisplayName());
            Log.d("debug","email"+user.getEmail());
            Log.d("debug","photoUrl "+photoUrl);


            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            userId = user.getUid();

        }
        return   userId;
    }


    @Override
    public void onClick(View view) {
        hostActivityInterface.hideBoard();

        if (view == binding.tvLogin){
            hostActivityInterface.popBackStack();
        }
        else if (view == binding.btnSignUp) {

            try {
                String email = binding.etEmail.getText().toString().trim();
                String name = binding.etUserName.getText().toString().trim();
                String pass = binding.etPassword.getText().toString().trim();
                String conPass = binding.etConfPass.getText().toString().trim();
                String phone=binding.etUserPhone.getText().toString().trim();

                if (!CommonUtils.isEmailValid(email)) {
                   // showDialog(getString(R.string.error_invalid_email), AppConst.AlertType.ERROR);
                    Toast.makeText(getContext(),"email invalid",Toast.LENGTH_SHORT).show();
                    binding.etEmail.setError("Invalid email");
                    binding.etEmail.requestFocus();
                    return;
                }

                if (pass.length() == 0) {
                   // showDialog(getString(R.string.error_pass), AppConst.AlertType.ERROR);
                    Toast.makeText(getContext(),"pass invalid",Toast.LENGTH_SHORT).show();
                    binding.etPassword.setError("Password must not be empty");
                    binding.etPassword.requestFocus();
                    return;
                }

                if (conPass.length() == 0) {
                   // showDialog(getString(R.string.error_conf_pass), AppConst.AlertType.ERROR);
                    Toast.makeText(getContext(),"pass invalid",Toast.LENGTH_SHORT).show();
                    binding.etPassword.setError("Password must not be empty");
                    binding.etPassword.requestFocus();
                    return;
                }

                if (pass.length() < 6 || conPass.length() < 6) {
                   // showDialog(getString(R.string.error_pass_min), AppConst.AlertType.ERROR);
                    Toast.makeText(getContext(),"pass invalid",Toast.LENGTH_SHORT).show();

                    return;
                }

                if (!pass.equals(conPass)) {
                //    showDialog(getString(R.string.error_pass_mismatch), AppConst.AlertType.ERROR);
                    Toast.makeText(getContext(),"miss math",Toast.LENGTH_SHORT).show();
                    binding.etPassword.setError("Password does not match");
                    binding.etPassword.requestFocus();
                    return;
                }

                if (name.length() == 0) {
                 //   showDialog(getString(R.string.error_first_name), AppConst.AlertType.ERROR);
                    Toast.makeText(getContext(),"fiels",Toast.LENGTH_SHORT).show();
                    binding.etUserName.setError("Empty field");
                    binding.etUserName.requestFocus();
                    return;
                }

                if (!binding.cbAgree.isChecked()) {
                   // showDialog(getString(R.string.error_accept), AppConst.AlertType.ERROR);
                    Toast.makeText(getContext(),"chk",Toast.LENGTH_SHORT).show();

                    return;
                }

                String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);


                AppUser appUser=new AppUser();
                appUser.setUFullName(name);
                appUser.setUEmail(email);
                appUser.setUPassword(pass);
                appUser.setuPhone(phone);
                if (CommonUtils.isNetworkAvailable(getActivity())) {

                    binding.setCanShowLoader(true);

                    creatUserAccount(appUser);




                } else {
                   // showDialog(getString(R.string.msg_check_internet), AppConst.AlertType.ERROR);
                    Toast.makeText(getContext(),"no internet connection  ",Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                Toast.makeText(getContext(),"error "+e,Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }

        } else if (view == binding.ivPicture) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getActivity(), camPermission[0]) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), camPermission[1]) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), camPermission[2]) != PackageManager.PERMISSION_GRANTED) {

                    // TODO: Consider calling
                    requestPermissions(camPermission,
                            AppConst.REQ_CAMERA);
                    return;
                }
            }

            showMenu();
        }
    }

    private void showMenu() {
        ProfilePicDialog dialog = new ProfilePicDialog(getActivity(), this);
        dialog.setCanShowRemove(encodeImage != null);
        dialog.show();

    }

    private void showDialog(String msg, int alertType) {
        hostActivityInterface.hideBoard();
        //sow dialogue
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub


        Runtime.getRuntime().gc();
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == AppConst.REQ_CAMERA || requestCode == AppConst.REQ_RETURN) {
                    if (data != null && data.getData() != null) {
                        selectedImageUri=data.getData();
                        setBitmapImage(data.getData());
                    } else {
                        showDialog("invalid image path", AppConst.AlertType.ERROR);
                    }
                } else if (requestCode == AppConst.REQ_GALLERY) {
                    if (data != null && data.getData() != null) {
                        BaseFragment fragment = EditSavePhotoFragment.newInstance(null, 0, null, false, data.getData());
                        fragment.setTargetFragment(SignUpFrag.this, AppConst.REQ_RETURN);
                        hostActivityInterface.addFragment(fragment, false);
                    } else {
                        showDialog("invalid image path", AppConst.AlertType.ERROR);

                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setBitmapImage(Uri data) {
        try {
            if (data != null) {
                picBitmap = loadBitmap(data.getPath());
                Runtime.getRuntime().gc();
                try {
                    try {
                        Picasso.with(getActivity())
                                .load(data).placeholder(R.drawable.ic_signup_logo).transform(new CircleTransform()).memoryPolicy(MemoryPolicy.NO_STORE)
                                .fit()
                                .centerInside()
                                .into(binding.ivPicture);
                    } catch (Exception e) {
                        binding.ivPicture.setImageResource(R.drawable.ic_signup_logo);

                    }

                     encodeImage = CommonUtils.rgetEncodedBitmapString(data);

                    if (encodeImage == null) {
                     //   showDialog(getString(R.string.invalid_image_path), AppConst.AlertType.ERROR);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
              //  showDialog(getString(R.string.invalid_image_path), AppConst.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onProfileResponse(int from) {
        switch (from) {

            case AppConst.PicType.CANCEL:
                break;
            case AppConst.PicType.CHOOSE:
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, AppConst.REQ_GALLERY);
                break;
            case AppConst.PicType.REMOVE:
                encodeImage = null;
                binding.ivPicture.setImageResource(R.drawable.ic_signup_logo);
                break;
            case AppConst.PicType.TAKE_PHOTO:
                Intent startCustomCameraIntent = new Intent(getActivity(), CameraActivity.class);
                startActivityForResult(startCustomCameraIntent, AppConst.REQ_CAMERA);
                break;
        }
    }




    class ClickTC extends ClickableSpan { // clickable span
        public void onClick(View textView) {
            // do something

            try {
                hostActivityInterface.hideBoard();
                BaseFragment fragment = new Frag2();
                Bundle bundle = new Bundle();
                bundle.putBoolean(AppConst.isWeb, false);
                bundle.putBoolean(AppConst.isTerms, true);
                bundle.putString(AppConst.serviceType, AppConst.termsConditionsEn);
                fragment.setArguments(bundle);
                hostActivityInterface.addFragment(fragment, true);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void updateDrawState(TextPaint ds) {

            ds.setColor(ContextCompat.getColor(getActivity(), R.color.colorTextOther));// set
            // text
            // color
            ds.setUnderlineText(true); // set to false to remove underline
        }
    }

    class ClickPP extends ClickableSpan { // clickable span
        public void onClick(View textView) {
            // do something

            try {
                hostActivityInterface.hideBoard();
                BaseFragment fragment = new Frag2();
                Bundle bundle = new Bundle();
                bundle.putBoolean(AppConst.isWeb, false);
                bundle.putBoolean(AppConst.isTerms, false);
                fragment.setArguments(bundle);
                hostActivityInterface.addFragment(fragment, true);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ContextCompat.getColor(getActivity(), R.color.colorTextOther));// set
            // text
            // color
            ds.setUnderlineText(true); // set to false to remove underline
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConst.REQ_CAMERA) {
// We have requested multiple permissions for contacts, so all of them need to be
            // checked.
            if (CommonUtils.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.
                showMenu();
            } else {
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        permissions[0]);
                if (!showRationale) {
                    showDialog("allow camera", AppConst.AlertType.PERMISSION_ERROR);
                }
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public Bitmap loadBitmap(String url)
    {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }

    public static Bitmap convertBitMaptoBase(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}
