package com.codepeaker.hora.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class AppUtils {
    private static AppUtils appUtils;

    private AppUtils() {
    }

    public static synchronized AppUtils getInstance() {
        if (appUtils == null) {
            appUtils = new AppUtils();
        }
        return appUtils;
    }


    private ProgressDialog progressDialog;

    public void showpDialog(Context context) {
        if (context != null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please Wait");
            if (!progressDialog.isShowing())
                progressDialog.show();
        }
    }


    public void hidepDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public Dialog getDialog(Context context, int layout) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        return dialog;
    }

    public String getEmailWithoutdot(String email) {
        email = email.replace(".", "dot");
        return email;
    }

    public boolean isValidFields(Context context, String email, String pass) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Please enter valid Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pass.isEmpty()) {
            Toast.makeText(context, "Please Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void setCameraPosition(GoogleMap mMap, ArrayList<LatLng> latLngs) {
        if (latLngs == null || latLngs.size() == 0)
            return;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngs) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 1000,1000,10));
    }


}
