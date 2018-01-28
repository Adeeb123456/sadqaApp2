package com.signup.frags;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
;import com.signup.R;
import com.signup.base.BaseFragment;
import com.signup.databinding.FragThankBinding;
import com.signup.utils.AppConst;

/**
 * Created by USER3 on 1/30/2017.
 */

public class ThankFrag extends BaseFragment implements View.OnClickListener {

    private FragThankBinding binding;

    @Override
    public String getTagText() {
        return null;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    public static ThankFrag getInstance(String msg) {
        ThankFrag frag = new ThankFrag();
        Bundle bundle = new Bundle();
        bundle.putString(AppConst.message, msg);
        frag.setArguments(bundle);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.frag_thank, container, false);
            try {
                binding.setMsg(getArguments().getString(AppConst.message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.btnContinue.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == binding.btnContinue) {
            //setAllowEnterTransitionOverlap(true);
        //    hostActivityInterface.showDrawerItemFragment(new LoginFragment());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
