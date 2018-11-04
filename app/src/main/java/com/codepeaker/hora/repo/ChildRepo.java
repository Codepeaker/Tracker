package com.codepeaker.hora.repo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.codepeaker.hora.activities.LoginActivity;
import com.codepeaker.hora.enums.FirebaseEnums;
import com.codepeaker.hora.model.ChildModel;
import com.codepeaker.hora.utils.AppUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChildRepo {
    private ChildRepo() {
    }

    private static ChildRepo childRepo;

    public static synchronized ChildRepo getInstance() {
        if (childRepo == null) {
            childRepo = new ChildRepo();
        }
        return childRepo;
    }

    public void registerChild(final Context context, String email, String password) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FirebaseEnums.CHILDREN.getValue()).child(email);
        ChildModel childModel = new ChildModel();
        childModel.setEmail(email);
        childModel.setPwd(password);
        AppUtils.getInstance().showpDialog(context);
        myRef.setValue(childModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "User Registered Successfully"
                        , Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
                AppUtils.getInstance().hidepDialog();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Please try again later"
                        , Toast.LENGTH_SHORT).show();
                AppUtils.getInstance().hidepDialog();
            }
        });
    }
}
