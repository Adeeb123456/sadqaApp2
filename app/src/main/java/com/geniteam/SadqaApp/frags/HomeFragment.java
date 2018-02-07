package com.geniteam.SadqaApp.frags;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.geniteam.SadqaApp.MainActivity;
import com.geniteam.SadqaApp.R;
import com.geniteam.SadqaApp.base.BaseFragment;
import com.geniteam.SadqaApp.base.DrawerItemBaseFragment;
import com.geniteam.SadqaApp.databinding.FragHomeBinding;
import com.geniteam.SadqaApp.model.SimInfo;
import com.geniteam.SadqaApp.sms.SadqaSmsManager;
import com.geniteam.SadqaApp.utils.AppConstants;
import com.geniteam.SadqaApp.utils.AppPref;
import com.geniteam.SadqaApp.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends DrawerItemBaseFragment implements View.OnClickListener {

    private FragHomeBinding binding;
    ArrayList<SimInfo> simInfosList;
    private boolean isInitial = true;
    SadqaSmsManager.SadqaSmsResponse sadqaSmsResponseCalback;

    int numberOfSmsToSend=0;

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

            binding.rlChoseAmount1.setOnClickListener(this);
            binding.rlChoseAmount2.setOnClickListener(this);
            binding.rlChoseAmount3.setOnClickListener(this);
            binding.rlChoseAmount4.setOnClickListener(this);
            binding.otherAmounts.setOnClickListener(this);
            binding.frequency.setOnClickListener(this);
            binding.setHomeScreenUiItem(null);
            binding.setUiItemList(null);
            binding.executePendingBindings();
        }

        return binding.getRoot();

    }
    double lat;
    double lon;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       if(hostActivityInterface.getMyLocation()!=null){
           lat=hostActivityInterface.getMyLocation().getLatitude();
         lon=hostActivityInterface.getMyLocation().getLongitude();
           Log.d("debug","lat "+lat);
           Log.d("debug","lon"+lon);

  // String countryName=      CommonUtils.getCountryName(getContext(),lat,lon);



           Log.i("debug","");

           new AddressAsyn().execute();
       }
       else {
           Utils.showSnackBarStatusFail(binding.getRoot(), "Fail to get Current Location ", "Retry", new Utils.SnackBarButtonListener() {
               @Override
               public void onBtnClickListener() {
                   try{
                       Intent intent=new Intent(getContext(),MainActivity.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(intent);
                   }catch (Exception e){
                       e.printStackTrace();
                   }

               }
           });
       }


     sadqaSmsResponseCalback=new SadqaSmsManager.SadqaSmsResponse() {
         @Override
         public void onSadqaSmsResponse(String statusMessage) {

         }
     };

    }

    @Override
    public void onClick(View view) {


        try {

            if(view==binding.rlChoseAmount1){
                numberOfSmsToSend=2;
                AppPref.putValueByKey(AppConstants.NUMBER_OF_SMS_TO_SEND,numberOfSmsToSend);
                Utils.showAlertDialogue(getActivity(), "", getString(R.string.alert_msg), new Utils.AlertDialogueClickListener() {
                    @Override
                    public void onOkClicked(int position) {
                      if(isRuntimePermissionGiven(android.Manifest.permission.READ_PHONE_STATE)){
                           simInfosList=getSimInfo();

                        }else {
                          requestRunTimePermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE},AppConstants.READ_PHONE_STATE_PERMISSION);

                      }
                        if(isRuntimePermissionGiven(android.Manifest.permission.SEND_SMS)){
                            sendSadqa();
                        }else {
                            requestRunTimePermissions(new String[]{android.Manifest.permission.SEND_SMS},AppConstants.SEND_SMS_PERMISSION);
                        }
                    }
                });
            }else if(view==binding.rlChoseAmount2){

            }else if(view==binding.rlChoseAmount3){

            }else if(view==binding.rlChoseAmount4){

            }else if(view==binding.otherAmounts){
                BaseFragment baseFragment=new HomeAddressFrag();
                hostActivityInterface.addFragment(baseFragment,true);
            }else if(view==binding.otherAmounts){
                BaseFragment baseFragment=new HomeAddressFrag();
                hostActivityInterface.addFragment(baseFragment,true);
            }else if(view==binding.frequency){
                BaseFragment baseFragment=new ChoseSmsFrequency();
                hostActivityInterface.addFragment(baseFragment,true);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendSadqa(){


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(simInfosList!=null&&simInfosList.size()>=1){

                    Utils.showAlertDialogForDuelSim(getActivity(), getSimInfo(), new Utils.AlertDialogueClickListener() {
                        @Override
                        public void onOkClicked(int position) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                                new SadqaSmsManager().sendSmsThroughSelectedSim(getContext(),simInfosList.get(position).subscriptionId,
                                        AppConstants.SADQA_SMS_NUMBER,numberOfSmsToSend,"",sadqaSmsResponseCalback);
                            }
                        }
                    });

                }else {

                    new SadqaSmsManager().sendSadqaThroughSms(getContext(),numberOfSmsToSend,
                            AppConstants.SADQA_SMS_NUMBER, "", sadqaSmsResponseCalback);
                }


            }
        },100);


    }



    public ArrayList<SimInfo>  getSimInfo(){

        ArrayList<SimInfo> simInfosList=null;
        SimInfo simInfo;
try{

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            simInfosList=new ArrayList<>();
            SubscriptionManager subscriptionManager = SubscriptionManager.from(getContext());
            List<SubscriptionInfo> subsInfoList = subscriptionManager.getActiveSubscriptionInfoList();

            Log.d("debug", "Current list = " + subsInfoList);

            for (SubscriptionInfo subscriptionInfo : subsInfoList) {
                simInfo=new SimInfo();
                CharSequence  carrierName=subscriptionInfo.getCarrierName();
                int subscriptionId=subscriptionInfo.getSubscriptionId();
                String number=subscriptionInfo.getNumber();

                simInfo.careerName=carrierName.toString();
                simInfo.subscriptionId=subscriptionId;
                simInfo.number=number;
                simInfosList.add(simInfo);
                Log.d("debug", " Number is  " + carrierName);
            }


        }else {
            simInfosList=null;
            Log.d("debug", "below lolly pop");
        }
}catch (Exception e){

}
        return simInfosList;
    }


    public void addOtherAmountFrag(){
        Bundle bundle = new Bundle();
        BaseFragment fragment = new HomeAddressFrag();

        hostActivityInterface.addFragment(fragment, true);;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


@BindingAdapter("fontStyle")
public static void setFontStyle(TextView textView,String fontopt){

}

    @BindingAdapter("fontColor")
    public  static void setFontColor(TextView textView,String fontopt){

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if(requestCode==AppConstants.READ_PHONE_STATE_PERMISSION){
            Toast.makeText(getContext(),"sms permitted",Toast.LENGTH_SHORT).show();
            getSimInfo();
            sendSadqa();
        }else if(requestCode==AppConstants.SEND_SMS_PERMISSION){
            //Toast.makeText(getContext(),"sms Permission Granted",Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(getContext(),"No Permission Granted",Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



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

    private boolean isRuntimePermissionGiven(String permisison){

        if(ContextCompat.checkSelfPermission(getContext(),permisison)== PackageManager.PERMISSION_GRANTED){
            return true;
        }else
            return false;
    }
    private void requestRunTimePermissions(String[] permission,int REQUESTCODE){
        try{
            requestPermissions(permission,REQUESTCODE);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



ProgressDialog progressDialog;
    public class AddressAsyn extends AsyncTask<Object, Object, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Getting Your current location");
            progressDialog.setMessage("please  wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Object... params) {

            String streetAddressbackground =null;
               streetAddressbackground     = getMyLocationAddress(lat, lon);
             Log.i("debug","streert adress"+streetAddressbackground);

            //  streetAddress=streetAddressbackground;
            //   UserLocalStore.setUserCurrentLocationSp(streetAddress,getApplicationContext());


            return streetAddressbackground;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            binding.address.setText(aVoid);

            if(aVoid==null){
                Utils.showSnackBarStatusFail(binding.getRoot(), "Fail to get Address ", "Retry", new Utils.SnackBarButtonListener() {
                    @Override
                    public void onBtnClickListener() {
                        Intent intent=new Intent(getContext(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
            //  orderItemInfoDispaly.setText(orderlist+"\n\nStreet Address: "+streetAddress+" : ");

        }
    }


    public String getMyLocationAddress(double lati,double longi) {
        String AddressStr=null;
        String country1;
        String city1;
        Geocoder geocoder= new Geocoder(getContext(), Locale.ENGLISH);

        try {

            //Place your latitude and longitude

            List<Address> addresses = geocoder.getFromLocation(lati,longi, 1);

            if(addresses != null) {

                Address fetchedAddress = addresses.get(0);
                String city,postal,province,particularPlace,district,sub1 ,s2,s3;
                String country="Country: "+addresses.get(0).getCountryName();
                country1=addresses.get(0).getCountryName();
                Log.i("debug","countryt "+country);
                province="Province: "+addresses.get(0).getAdminArea();
                city="City: "+addresses.get(0).getLocality();
                city1=addresses.get(0).getLocality();;
                postal="Postal: "+addresses.get(0).getPostalCode();
                particularPlace="Current Location: "+addresses.get(0).getFeatureName(); // eg mirpurabbott, labs road
                district="District: "+addresses.get(0).getSubAdminArea();
                //  district="District: "+addresses.get(0).getSubLocality();

                StringBuilder strAddress = new StringBuilder();

                for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                }

                AddressStr=" " + strAddress.toString()+country+"\n"+province+"\n"+city+"\n"+particularPlace;


            }

            else {
                AddressStr=null;
            }
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            AddressStr=null;
        }

        return AddressStr;
    }


}


