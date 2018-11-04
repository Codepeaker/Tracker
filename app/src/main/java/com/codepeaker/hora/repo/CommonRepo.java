package com.codepeaker.hora.repo;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.codepeaker.hora.enums.FirebaseEnums;
import com.codepeaker.hora.utils.AppUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CommonRepo {
    private CommonRepo() {
    }

    private static CommonRepo commonRepo;

    public static synchronized CommonRepo getInstance() {
        if (commonRepo == null) {
            commonRepo = new CommonRepo();
        }
        return commonRepo;
    }

    private MutableLiveData<Object> mapMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Object> signInUser(final Application application
            , final FirebaseEnums firebaseEnum, final String password, final String email) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;
        myRef = database.getReference(firebaseEnum.getValue());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AppUtils.getInstance().hidepDialog();
                String user = AppUtils.getInstance().getEmailWithoutdot(email);
                if (dataSnapshot.hasChild(user)) {
                    dataSnapshot = dataSnapshot.child(user);
                    if (dataSnapshot.child("pwd").getValue().toString().equals(password)) {
                        Object ob = dataSnapshot.getValue();
                        mapMutableLiveData.setValue(ob);
                    } else {
                        Toast.makeText(application, "Invalid Cred"
                                , Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(application, "User not found"
                            , Toast.LENGTH_SHORT).show();
                }
                AppUtils.getInstance().hidepDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(application, "Login Cancelled", Toast.LENGTH_SHORT).show();
                AppUtils.getInstance().hidepDialog();
            }
        });

        return mapMutableLiveData;

    }
}
