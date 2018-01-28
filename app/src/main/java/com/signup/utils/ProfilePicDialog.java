package com.signup.utils;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.signup.R;
import com.signup.databinding.DialogPictureBinding;


/**
 * Created by USER3 on 2/18/2017.
 */

public class ProfilePicDialog extends Dialog implements View.OnClickListener {

    private onProfilePicResponse listener;
    private Context context;
    private DialogPictureBinding binding;
    private boolean canShowRemove;

    public interface onProfilePicResponse {
        void onProfileResponse(int from);
    }

    public ProfilePicDialog(Context context, onProfilePicResponse listener) {
        super(context, R.style.pic_dialog);
        this.context = context;
        this.listener = listener;
    }

    public void setCanShowRemove(boolean canShowRemove) {
        this.canShowRemove = canShowRemove;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_picture, null, false);
        binding.setCanShowRemove(canShowRemove);
        setContentView(binding.getRoot());

        binding.tvTakePhoto.setOnClickListener(this);
        binding.tvChoose.setOnClickListener(this);
        binding.tvRemove.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (listener != null) {
            switch (view.getId()) {
                case R.id.tvCancel:
                    listener.onProfileResponse(AppConst.PicType.CANCEL);
                    break;
                case R.id.tvChoose:
                    listener.onProfileResponse(AppConst.PicType.CHOOSE);
                    break;
                case R.id.tvTakePhoto:
                    listener.onProfileResponse(AppConst.PicType.TAKE_PHOTO);
                    break;
                case R.id.tvRemove:
                    listener.onProfileResponse(AppConst.PicType.REMOVE);
                    break;
            }
        }
        dismiss();
    }
}
