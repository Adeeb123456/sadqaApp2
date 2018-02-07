package com.geniteam.SadqaApp.frags;

import android.Manifest;
import android.app.Activity;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.geniteam.SadqaApp.R;
import com.geniteam.SadqaApp.databinding.FragProfileBinding;
import com.google.gson.Gson;
import com.geniteam.SadqaApp.CameraActivity;
import com.geniteam.SadqaApp.base.BaseFragment;
import com.geniteam.SadqaApp.camerautils.EditSavePhotoFragment;

import com.geniteam.SadqaApp.utils.AppConst;
import com.geniteam.SadqaApp.utils.AppPref;
import com.geniteam.SadqaApp.utils.CircleTransform;
import com.geniteam.SadqaApp.utils.CommonUtils;
import com.geniteam.SadqaApp.utils.HideShowKeyPad;
import com.geniteam.SadqaApp.model.GetLogin;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.geniteam.SadqaApp.base.DrawerItemBaseFragment;
import com.geniteam.SadqaApp.utils.ProfilePicDialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.geniteam.SadqaApp.AppBase.service;


public class ProfileFrag extends DrawerItemBaseFragment implements View.OnClickListener, ProfilePicDialog.onProfilePicResponse {

    private FragProfileBinding binding;
    private boolean isInitial = true;
    private String encodeImage;
    public static String[] camPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    public String getTagText() {
        return null;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.frag_profile, container, false);
            ((AppCompatActivity) getActivity()).setSupportActionBar(binding.include.toolbar);
            binding.include.toolbar.setNavigationIcon(R.drawable.ic_humberg_menu);
            binding.include.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hostActivityInterface.openDrawer();
                }
            });
        }

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isInitial) {
            new HideShowKeyPad().setupUI(binding.parent);

            binding.btnUpdate.setOnClickListener(this);
            binding.btnChangePass.setOnClickListener(this);
            binding.ivPicture.setOnClickListener(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isInitial) {

            bindData();

            binding.etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        hostActivityInterface.hideBoard();
                        sendRequest();
                        return true;
                    }
                    return false;
                }
            });
            isInitial = false;

            getUserDetail();
        }
    }

    private void bindData() {
        try {
            if (hostActivityInterface.getUser() != null) {
                binding.etUserName.setText(hostActivityInterface.getUser().getUFullName());
                binding.etEmail.setText(hostActivityInterface.getUser().getUEmail());
            }
            setProfileImage(binding.ivPicture, hostActivityInterface.getUser().getUImageURL().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRequest() {

        try {
            String password = "";
            if (hostActivityInterface.getUser() == null) {
              //  showDialog(getString(R.string.error_not_login), AppConst.AlertType.ERROR);
              //  hostActivityInterface.showDrawerItemFragment(new LoginFragment());

                return;
            }

            String email = binding.etEmail.getText().toString().trim();
            String userName = binding.etUserName.getText().toString().trim();

            if (!CommonUtils.isEmailValid(email)) {
              //  showDialog(getString(R.string.error_invalid_email), AppConst.AlertType.ERROR);
                return;
            }

            if (userName.length() == 0) {
               // showDialog(getString(R.string.error_first_name), AppConst.AlertType.ERROR);
                return;
            }

            password = hostActivityInterface.getUser().getUPassword();
            String android_id = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

            if (CommonUtils.isNetworkAvailable(getActivity())) {

                binding.setCanShowLoader(true);


                        try {

                                //    Gson gson = new Gson();
                                //    String json = gson.toJson(login.getAppUser());
                                 //   AppPref.putValueByKey(AppConst.keyUser, json);
                                 //   hostActivityInterface.popBackStack();
                                 //   AppPref.saveUserModel(login);
                                    hostActivityInterface.setUserUI();
                                 //   showDialog(login.getSuccessMessage(), AppConst.AlertType.SUCCESS);
                                    hostActivityInterface.showDrawerItemFragment(new HomeFragment());


                        } catch (Exception e) {
                            e.printStackTrace();
                          //  showDialog(getString(R.string.error_undone), AppConst.AlertType.ERROR);
                        }
                        binding.setCanShowLoader(false);
                    }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUserDetail() {

        try {
            if (CommonUtils.isNetworkAvailable(getActivity())) {

                Call<GetLogin> call = service.getUserDetail(hostActivityInterface.getUser().getUserID().toString());

                call.enqueue(new Callback<GetLogin>() {
                    @Override
                    public void onResponse(Call<GetLogin> call, Response<GetLogin> response) {

                        try {
                            GetLogin login = response.body();
                            if (login != null) {
                                if (login.state) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(login.content);
                                    AppPref.putValueByKey(AppConst.keyUser, json);
                                    hostActivityInterface.setUserUI();
                                    bindData();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onFailure(Call<GetLogin> call, Throwable t) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        hostActivityInterface.hideBoard();
        if (view == binding.btnChangePass) {
            hostActivityInterface.addFragment(new Frag2(), true);
        } else if (view == binding.btnUpdate) {
            sendRequest();
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
        if (hostActivityInterface.getUser() != null) {
            ProfilePicDialog dialog = new ProfilePicDialog(getActivity(), this);
            dialog.show();
        } else {
           // showDialog(getString(R.string.error_not_login), AppConst.AlertType.ERROR);
           // hostActivityInterface.showDrawerItemFragment(new LoginFragment());
            Toast.makeText(getContext(),"login plz",Toast.LENGTH_SHORT).show();
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
                    showDialog("ALLOW CAMERA", AppConst.AlertType.PERMISSION_ERROR);
                }
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showDialog(String msg, int alertType) {
        hostActivityInterface.hideBoard();
      //  MyDialog dialog = new MyDialog(getActivity(), this, alertType, msg);
     //   dialog.show();
    }

    @Override
    public void onProfileResponse(@AppConst.PicType int from) {

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
                break;
            case AppConst.PicType.TAKE_PHOTO:
                Intent startCustomCameraIntent = new Intent(getActivity(), CameraActivity.class);
                startActivityForResult(startCustomCameraIntent, AppConst.REQ_CAMERA);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub


        Runtime.getRuntime().gc();
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == AppConst.REQ_CAMERA || requestCode == AppConst.REQ_RETURN) {
                    if (data != null && data.getData() != null) {
                        uploadBitmapFromUri(data.getData(), false);
                    } else {
                        showDialog("INVALID IMAGE PATH", AppConst.AlertType.ERROR);
                    }
                } else if (requestCode == AppConst.REQ_GALLERY) {
                    if (data != null && data.getData() != null) {
                        BaseFragment fragment = EditSavePhotoFragment.newInstance(null, 0, null, false, data.getData());
                        fragment.setTargetFragment(ProfileFrag.this, AppConst.REQ_RETURN);
                        hostActivityInterface.addFragment(fragment, false);
                    } else {
                     //   showDialog(getString("INVALID IMAGE PATH", AppConst.AlertType.ERROR);

                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void uploadBitmapFromUri(Uri data, boolean canAllowEmpty) {
        try {
            if (data != null) {
                Runtime.getRuntime().gc();
                try {
                    try {
                        Picasso.with(getActivity())
                                .load(data).placeholder(R.drawable.ic_review_holder).transform(new CircleTransform()).memoryPolicy(MemoryPolicy.NO_STORE)
                                .fit()
                                .centerInside()
                                .into(binding.ivPicture);
                    } catch (Exception e) {
                        binding.ivPicture.setImageResource(R.drawable.ic_review_holder);

                    }
                    String encodeImage = CommonUtils.rgetEncodedBitmapString(data);
                    this.encodeImage = encodeImage;

//                    if (encodeImage != null) {
//
//                        if (CommonUtils.isNetworkAvailable(getActivity())) {
//
//                            Map<String, String> map = new HashMap<>();
//                            map.put(AppConst.serviceType, AppConst.GET_UPLOAD_IMAGE);
//                            map.put(AppConst.extension, "jpg");
//                            map.put(AppConst.customerId, hostActivityInterface.getUser().getUserID().toString());
//                            map.put(AppConst.image, encodeImage);
//                            binding.setCanShowLoader(true);
//                            Call<GetImage> call = service.uploadImage(map);
//
//                            call.enqueue(new Callback<GetImage>() {
//                                @Override
//                                public void onResponse(Call<GetImage> call, Response<GetImage> response) {
//
//                                    try {
//                                        GetImage status = response.body();
//                                        if (status != null) {
//                                            if (status.state) {
//                                                Picasso.with(getActivity()).invalidate(hostActivityInterface.getUser().getUImageURL().toString());
//                                                hostActivityInterface.getUser().setUImageURL(status.content.getImageUrl());
//                                                Gson gson = new Gson();
//                                                String json = gson.toJson(hostActivityInterface.getUser());
//                                                AppPref.putValueByKey(AppConst.keyUser, json);
//                                                setProfileImage(binding.ivPicture, status.content.getImageUrl());
//                                                hostActivityInterface.setUserUI();
//                                            } else {
//                                                showDialog(status.CommonError.getMessage(), AppConst.AlertType.ERROR);
//                                            }
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                        showDialog(getString(R.string.error_undone), AppConst.AlertType.ERROR);
//                                    }
//                                    binding.setCanShowLoader(false);
//                                }
//
//                                @Override
//                                public void onFailure(Call<GetImage> call, Throwable t) {
//
//                                    showDialog(getString(R.string.error_undone), AppConst.AlertType.ERROR);
//                                    binding.setCanShowLoader(false);
//                                }
//                            });
//
//                        } else {
//                            showDialog(getString(R.string.msg_check_internet), AppConst.AlertType.ERROR);
//                        }
//                    } else {
//                        showDialog(getString(R.string.invalid_image_path), AppConst.AlertType.ERROR);
//                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                showDialog("INVALID IMAGE PATH", AppConst.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setProfileImage(ImageView view, String url) {
        try {
            Picasso.with(view.getContext())
                    .load(url).placeholder(R.drawable.ic_username).transform(new CircleTransform())
                    .fit()
                    .centerInside()
                    .into(view);
        } catch (Exception e) {
           // view.setImageResource(R.drawable.ic_username);

        }
    }



    public Bitmap loadBitmap(String path) {
        File file = new File(path);

        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(file.getAbsolutePath()).openConnection();
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
}
