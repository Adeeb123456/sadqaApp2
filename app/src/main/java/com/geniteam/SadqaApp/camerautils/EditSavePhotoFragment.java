package com.geniteam.SadqaApp.camerautils;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.geniteam.SadqaApp.R;
import com.geniteam.SadqaApp.databinding.SquarecameraFragmentEditSavePhotoBinding;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.geniteam.SadqaApp.CameraActivity;
import com.geniteam.SadqaApp.base.BaseFragment;




/**
 *
 */
public class EditSavePhotoFragment extends BaseFragment implements CropImageView.OnGetCroppedImageCompleteListener {

    public static final String TAG = EditSavePhotoFragment.class.getSimpleName();
    public static final String BITMAP_KEY = "bitmap_byte_array";
    public static final String ROTATION_KEY = "rotation";
    public static final String IMAGE_INFO = "image_info";
    public static final String IS_FROM_CAMERA = "is_from_camera";
    public static final String PATH = "path";

    private static final int REQUEST_STORAGE = 1;

    private CropImageView photoImageView;

    private boolean isCamera;

    private Uri pathUri;

    private SquarecameraFragmentEditSavePhotoBinding binding;

    public static BaseFragment newInstance(byte[] bitmapByteArray, int rotation,
                                           @NonNull ImageParameters parameters, boolean from, Uri data) {
        BaseFragment fragment = new EditSavePhotoFragment();

        Bundle args = new Bundle();
        args.putByteArray(BITMAP_KEY, bitmapByteArray);
        args.putInt(ROTATION_KEY, rotation);
        args.putParcelable(IMAGE_INFO, parameters);
        args.putBoolean(IS_FROM_CAMERA, from);
        if (data != null) {
            args.putString(PATH, data.toString());
        }

        fragment.setArguments(args);
        return fragment;
    }

    public EditSavePhotoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.squarecamera__fragment_edit_save_photo,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        photoImageView = (CropImageView) binding.getRoot().findViewById(R.id.photo);
        photoImageView.setGuidelines(CropImageView.Guidelines.OFF);
        photoImageView.setFixedAspectRatio(true);
        photoImageView.setShowCropOverlay(true);


        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        int rotation = getArguments().getInt(ROTATION_KEY);
        byte[] data = getArguments().getByteArray(BITMAP_KEY);
        ImageParameters imageParameters = getArguments().getParcelable(IMAGE_INFO);
        isCamera = getArguments().getBoolean(IS_FROM_CAMERA);

        if (getArguments().containsKey(PATH)) {
            pathUri = Uri.parse(getArguments().getString(PATH));
        }
        try {
            if (imageParameters != null) {

                imageParameters.mIsPortrait =
                        getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

                final View topView = view.findViewById(R.id.topView);
                if (imageParameters.mIsPortrait) {
                    topView.getLayoutParams().height = imageParameters.mCoverHeight;
                } else {
                    topView.getLayoutParams().width = imageParameters.mCoverWidth;
                }
            }


            Runtime.getRuntime().gc();
            if (data != null) {
                rotatePicture(rotation, data);
            } else {
                photoImageView.setOnGetCroppedImageCompleteListener(this);
                photoImageView.setImageUriAsync(pathUri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        view.findViewById(R.id.save_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePicture();
            }
        });
    }

    private void rotatePicture(int rotation, byte[] data) {
        try {
            Bitmap bitmap = ImageUtility.decodeSampledBitmapFromByte(getActivity(), data);
//        Log.d(TAG, "original bitmap width " + bitmap.getWidth() + " height " + bitmap.getHeight());
            if (rotation != 0) {
                Bitmap oldBitmap = bitmap;

                Matrix matrix = new Matrix();
                matrix.postRotate(rotation);

                bitmap = Bitmap.createBitmap(
                        oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, false
                );

                oldBitmap.recycle();
            }

            photoImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void savePicture() {
        requestForPermission();
    }

    private void requestForPermission() {
        RuntimePermissionActivity.startActivity(EditSavePhotoFragment.this,
                REQUEST_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK != resultCode) return;

        if (REQUEST_STORAGE == requestCode && data != null) {
            try {
                final boolean isGranted = data.getBooleanExtra(RuntimePermissionActivity.REQUESTED_PERMISSION, false);

                if (isGranted) {
                    Bitmap bitmap = photoImageView.getCroppedImage();
                    if (bitmap == null) {
                        photoImageView.getCroppedImageAsync();
                        return;
                    }
                    saveExit(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void saveExit(Bitmap bitmap) {
        try {
            if (bitmap != null) {
                Uri photoUri =ImageUtility.savePicture(getActivity(), bitmap);
                if (isCamera) {
                    ((CameraActivity) getActivity()).returnPhotoUri(photoUri);
                } else {
                    Intent intent = new Intent();
                    intent.setData(photoUri);
                    getTargetFragment().onActivityResult(getTargetRequestCode(),
                            Activity.RESULT_OK, intent);
                    getFragmentManager().popBackStack();
                }
            } else {
                //showDialog(getString(R.string.invalid_image_path), AppConst.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialog(String msg, int alertType) {
   //     MyDialog dialog = new MyDialog(getActivity(), null, alertType, msg);
    //    dialog.show();
    }

    @Override
    public void onGetCroppedImageComplete(CropImageView view, Bitmap bitmap, Exception error) {

        if (bitmap != null) {
            saveExit(bitmap);
        } else {
          //  showDialog(getString(R.string.invalid_image_path), AppConst.AlertType.ERROR);
        }

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
