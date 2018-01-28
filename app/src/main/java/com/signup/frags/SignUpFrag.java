package com.signup.frags;

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
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.signup.CameraActivity;
import com.signup.R;
import com.signup.camerautils.EditSavePhotoFragment;
import com.signup.databinding.FragSignupBinding;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.signup.AppBase;
import com.signup.model.registermodel.SignUpModel;
import com.signup.utils.AppConst;
import com.signup.utils.ProfilePicDialog;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.signup.base.BaseFragment;


import com.signup.utils.CircleTransform;
import com.signup.utils.CommonUtils;
import com.signup.utils.HideShowKeyPad;

import static com.signup.frags.ProfileFrag.camPermission;


public class SignUpFrag extends BaseFragment implements View.OnClickListener, ProfilePicDialog.onProfilePicResponse {

    private FragSignupBinding binding;
    private String encodeImage = "";
    public Bitmap picBitmap;

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

                if (!CommonUtils.isEmailValid(email)) {
                   // showDialog(getString(R.string.error_invalid_email), AppConst.AlertType.ERROR);
                    Toast.makeText(getContext(),"email invalid",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.length() == 0) {
                   // showDialog(getString(R.string.error_pass), AppConst.AlertType.ERROR);
                    Toast.makeText(getContext(),"pass invalid",Toast.LENGTH_SHORT).show();

                    return;
                }

                if (conPass.length() == 0) {
                   // showDialog(getString(R.string.error_conf_pass), AppConst.AlertType.ERROR);
                    Toast.makeText(getContext(),"pass invalid",Toast.LENGTH_SHORT).show();

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

                    return;
                }

                if (name.length() == 0) {
                 //   showDialog(getString(R.string.error_first_name), AppConst.AlertType.ERROR);
                    Toast.makeText(getContext(),"fiels",Toast.LENGTH_SHORT).show();

                    return;
                }

                if (!binding.cbAgree.isChecked()) {
                   // showDialog(getString(R.string.error_accept), AppConst.AlertType.ERROR);
                    Toast.makeText(getContext(),"chk",Toast.LENGTH_SHORT).show();

                    return;
                }

                String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                if (CommonUtils.isNetworkAvailable(getActivity())) {

                    binding.setCanShowLoader(true);

                    Call<SignUpModel> call = AppBase.service.postSignupUser(name,email,pass,android_id,"Android",encodeImage);

                    call.enqueue(new Callback<SignUpModel>() {
                        @Override
                        public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {

                            try {
                                SignUpModel status = response.body();
                                if (status != null) {
                                    if (status.getSuccess()) {
                                        hostActivityInterface.addFragment(ThankFrag.getInstance(status.getAppUser().getUFullName()), true);
                                    } else {
                                        Toast.makeText(getContext(),"error "+status.getError().getMessage(),Toast.LENGTH_SHORT).show();

                                        showDialog(status.getError().getMessage(), AppConst.AlertType.ERROR);
                                    }
                                }
                            } catch (Exception e) {
                                Toast.makeText(getContext(),"error 2"+e,Toast.LENGTH_SHORT).show();

                                e.printStackTrace();
                               // showDialog(getString(R.string.error_undone), AppConst.AlertType.ERROR);
                            }
                            binding.setCanShowLoader(false);
                        }

                        @Override
                        public void onFailure(Call<SignUpModel> call, Throwable t) {
                            Toast.makeText(getContext(),"error "+t,Toast.LENGTH_SHORT).show();

                            //showDialog(getString(R.string.error_undone), AppConst.AlertType.ERROR);
                            binding.setCanShowLoader(false);
                        }
                    });
                } else {
                   // showDialog(getString(R.string.msg_check_internet), AppConst.AlertType.ERROR);
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
