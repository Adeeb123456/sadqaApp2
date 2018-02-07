package com.geniteam.SadqaApp.frags;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.geniteam.SadqaApp.AppBase;
import com.geniteam.SadqaApp.R;
import com.geniteam.SadqaApp.base.DrawerItemBaseFragment;
import com.geniteam.SadqaApp.databinding.FragLoginBinding;
import com.geniteam.SadqaApp.model.login.AppUser;
import com.geniteam.SadqaApp.utils.CommonUtils;
import com.geniteam.SadqaApp.utils.HideShowKeyPad;
import com.geniteam.SadqaApp.webservice.FireBaseTransaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;


public class LoginFragment extends DrawerItemBaseFragment implements View.OnClickListener {

    private FragLoginBinding binding;
    private boolean canReturnData;

    @Override
    public String getTagText() {
        return "LoginFragment";
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.frag_login, container, false);
            binding.setCustomMargin(-100);
            ((AppCompatActivity) getActivity()).setSupportActionBar(binding.include.toolbar);

            try {
              //  canReturnData = getArguments().getBoolean(AppConst.keyReturn);
                binding.include.toolbar.setNavigationIcon(R.drawable.ic_back);
            } catch (Exception e) {
                binding.include.toolbar.setNavigationIcon(R.drawable.ic_humberg_menu);
            }


            binding.include.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (canReturnData) {
                        hostActivityInterface.popBackStack();
                    } else {
                        hostActivityInterface.openDrawer();
                    }

                }
            });

        }

        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        binding.tvSignUp.setOnClickListener(this);
        binding.tvForgetPass.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        new HideShowKeyPad().setupUI(binding.parent);
    }

    @Override
    public void onClick(View view) {

        hostActivityInterface.hideBoard();
        if (view == binding.tvSignUp) {
           // hostActivityInterface.addFragment(new StartFragment(), true);
            hostActivityInterface.showDrawerItemFragment(new StartFragment());
        } else if (view == binding.tvForgetPass) {
            hostActivityInterface.addFragment(new ForgetPassFrag(), true);
        } else if (view == binding.btnLogin) {

            String email = binding.etEmail.getText().toString().trim();
            String pass = binding.etPassword.getText().toString().trim();

//            if (!CommonUtils.isEmailValid(email)) {
//                showDialog(getString(R.string.error_invalid_email), AppConst.AlertType.ERROR);
//                return;
//            }

            if (pass.length() == 0) {
              //  showDialog(getString(R.string.error_pass), AppConst.AlertType.ERROR);
                return;
            }

            if (pass.length() < 2) {
            //    showDialog(getString(R.string.error_pass_min), AppConst.AlertType.ERROR);
                return;
            }



            if (CommonUtils.isNetworkAvailable(getActivity())) {

                binding.setCanShowLoader(true);


                        try {

                            AppBase.mFirebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                      //  Gson gson=new Gson();
                                       // String json = gson.toJson(gson);
                                      //  AppPref.putValueByKey(AppConst.keyUser, json);
                                      //  AppPref.saveUserModel(login);
                            String uid=  AppBase.mFirebaseAuth.getCurrentUser().getUid();

                                        FireBaseTransaction.fetchFireBaseUser(getContext(), uid, new FireBaseTransaction.FetchUserCallback() {
                                            @Override
                                            public void onFireBaseResponse(AppUser appUser, boolean isSuccess) {
                                                hostActivityInterface.setUpDrawerUi(appUser);

                                                hostActivityInterface.showDrawerItemFragment(new HomeFragment());
                                                binding.setCanShowLoader(false);

                                            }

                                            @Override
                                            public void onFailure(AppUser appUser, boolean isSuccess, String errorMsg) {
                                                Toast.makeText(getContext()," "+errorMsg,Toast.LENGTH_LONG).show();
                                                binding.setCanShowLoader(false);

                                            }
                                        });



                                    }else if (null!=task.getException().getMessage()){
                                        binding.setCanShowLoader(false);
                                        Toast.makeText(getContext(),"Fail to login "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });



                        } catch (Exception e) {
                            e.printStackTrace();

                        }



            } else {
               // showDialog(getString(R.string.msg_check_internet), AppConst.AlertType.ERROR);
            }

        }
    }

    private void showDialog(String msg, int alertType) {
        hostActivityInterface.hideBoard();
       // MyDialog dialog = new MyDialog(getActivity(), null, alertType, msg);
      //  dialog.show();
    }
}