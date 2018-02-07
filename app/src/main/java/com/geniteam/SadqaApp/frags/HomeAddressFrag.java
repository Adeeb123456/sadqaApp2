package com.geniteam.SadqaApp.frags;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geniteam.SadqaApp.R;
import com.geniteam.SadqaApp.base.BaseFragment;
import com.geniteam.SadqaApp.databinding.HomeAddressFragBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by 7CT on 2/2/2018.
 */

public class HomeAddressFrag extends BaseFragment implements OnMapReadyCallback {
    HomeAddressFragBinding binding;
    GoogleMap map;
    String userHomeAddress="";

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

        if(binding==null){

            binding= DataBindingUtil.inflate(inflater, R.layout.home_address_frag,container,false);
            binding.mapView.onCreate(savedInstanceState);
            binding.mapView.getMapAsync(this);
        }


        return binding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        binding.progressBar3.setVisibility(View.GONE);
        map=googleMap;
        Location location=hostActivityInterface.getMyLocation();
        if(map!=null){
            if(location!=null){
                map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude()))
                        .snippet(userHomeAddress).visible(true));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                        location.getLongitude()),16));


            }
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        try{
            if(binding.mapView!=null){
                binding.mapView.onLowMemory();
            }
        }catch (Exception e){

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(binding.mapView!=null){
            binding.mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(binding.mapView!=null){
            binding.mapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(binding.mapView!=null){
            binding.mapView.onDestroy();
        }
    }
}
