package com.geniteam.SadqaApp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;


import com.geniteam.SadqaApp.R;
import com.geniteam.SadqaApp.model.SimInfo;

import java.util.ArrayList;

/**
 * Created by 7CT on 1/26/2018.
 */

public class Utils {
public interface  SnackBarButtonListener{
    public void onBtnClickListener();
}
public  interface AlertDialogueClickListener{
    public void onOkClicked(int position);
}

    public static void showSnackBarStatusFail(View view, String message, String actionButtonTxt, final SnackBarButtonListener listener){

             Snackbar snackbar =
                    Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction(actionButtonTxt, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        if(listener!=null){
                            listener.onBtnClickListener();
                        }
                        }
                    });
            snackbar.show();
        }



        public static void showSnackBarStatusSuccess(View view,String successMessage,String actionButtonTxt){
           Snackbar snackbar= Snackbar.make(view,successMessage, Snackbar.LENGTH_SHORT).setAction(actionButtonTxt, new View.OnClickListener() {
               @Override
               public void onClick(View view) {

               }
           }) ;
        }



        public static  void showAlertDialogue(Activity activity, String title, String msg,
                                      final AlertDialogueClickListener clickListener){
            try{
                AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                builder.setTitle(title);
                builder.setMessage(msg);

                builder.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clickListener.onOkClicked(i);
                    }
                });

                builder.setNegativeButton(activity.getString(R.string.cancel), null);
                Dialog dialog=builder.create();
                dialog.getWindow().getAttributes().windowAnimations= R.style.alertDialogueAnimatuion;
                dialog.show();
            }catch (Exception e){
                e.printStackTrace();
            }


        }

    public static void showAlertDialogForDuelSim(Activity activity , final ArrayList<SimInfo> simInfosList, final AlertDialogueClickListener clickListener){

      try{


        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.select_sim));
        builder.setNegativeButton("Cancel",null);
        ArrayAdapter arrayAdapter=new ArrayAdapter(activity,android.R.layout.simple_selectable_list_item);
        for(SimInfo simInfo:simInfosList){
            arrayAdapter.add(simInfo.careerName);
        }
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                clickListener.onOkClicked(i);

            }
        });

        builder.show();

        Dialog dialog=builder.create();
        dialog.getWindow().getAttributes().windowAnimations=R.style.alertDialogueAnimatuion;
        dialog.show();

      }catch (Exception e){

      }
      }



}


