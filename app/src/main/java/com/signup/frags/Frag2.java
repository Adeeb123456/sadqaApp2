package com.signup.frags;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.signup.R;
import com.signup.base.DrawerItemBaseFragment;
import com.signup.databinding.FragTwoBinding;


/**
 * Created by guestsAll on 1/12/2018.
 */

public class Frag2 extends DrawerItemBaseFragment {
    FragTwoBinding fragTwoBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (fragTwoBinding == null) {
            fragTwoBinding = DataBindingUtil.inflate(inflater, R.layout.frag_two, container, false);
            ((AppCompatActivity) getActivity()).setSupportActionBar(fragTwoBinding.include.toolbar);
            fragTwoBinding.include.toolbar.setNavigationIcon(R.drawable.ic_humberg_menu);
            fragTwoBinding.include.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hostActivityInterface.openDrawer();
                }
            });
        }
        return fragTwoBinding.getRoot();
    }

    @Override
    public String getTagText() {
        return null;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
