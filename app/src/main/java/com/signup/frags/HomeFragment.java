package com.signup.frags;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.signup.R;
import com.signup.base.BaseFragment;
import com.signup.base.DrawerItemBaseFragment;
import com.signup.databinding.FragHomeBinding;

import java.util.ArrayList;





public class HomeFragment extends DrawerItemBaseFragment implements View.OnClickListener {

    private FragHomeBinding binding;
   // private ArrayList<VideoItem> videoItems;
   // private List<VideoItem> arraylistVideo;
   // private List<VideosLogo> arraylistVideoLogo;

    //private SnapRecyclerAdapter adapter;
    private boolean isInitial = true;//isAnimated;
    //private Animation animation;

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
        setRetainInstance(true);
        if (isInitial) {
          //  videoItems = new ArrayList<>();
           // arraylistVideo = new ArrayList<>();
           /* animation = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.fragment_slide_in_left);*/
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (binding == null) {
          //  Log.d("view", "is null");
            binding = DataBindingUtil.inflate(inflater, R.layout.frag_home, container, false);

            ((AppCompatActivity) getActivity()).setSupportActionBar(binding.include.toolbar);
            binding.include.toolbar.setNavigationIcon(R.drawable.ic_humberg_menu);
            binding.include.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hostActivityInterface.openDrawer();
                }
            });

            binding.buttonnextfrg.setOnClickListener(this);
        }

        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isInitial) {

            isInitial = false;







           // getVideos();

        }

    }

    public void addCompanyDetailFrag(String companyNumber){
        Bundle bundle = new Bundle();
        BaseFragment fragment = new HomeFragment();

        hostActivityInterface.addFragment(fragment, true);;
    }

    @Override
    public void onResume() {
        super.onResume();

    }



    @Override
    public void onClick(View view) {


        try {

            if(view==binding.buttonnextfrg){
                BaseFragment baseFragment=new FragWithOutDrawerMenu();
                hostActivityInterface.addFragment(baseFragment,true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void showDialog(String msg, int alertType) {
        hostActivityInterface.hideBoard();
        if (getActivity() != null) {
            //MyDialog dialog = new MyDialog(getActivity(), null, alertType, msg);
            //dialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == 1) {
                    if (data != null) {
                        ArrayList<Integer> catId = data.getIntegerArrayListExtra("id");
                        if (catId != null && catId.size()!=0) {
                            BaseFragment fragment = new HomeFragment();
                            fragment.setArguments(data.getExtras());
                            hostActivityInterface.addFragment(fragment, true);
                        }
                    }
                } else if (requestCode == 2) {
                    if (data != null) {

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
