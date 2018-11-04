package com.codepeaker.hora.repo;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.codepeaker.hora.activities.LoginActivity;
import com.codepeaker.hora.enums.FirebaseEnums;
import com.codepeaker.hora.model.ChildModel;
import com.codepeaker.hora.model.ParentModel;
import com.codepeaker.hora.utils.AppUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParentRepo {
    private FirebaseDatabase firebaseDatabase;

    private ParentRepo() {
    }

    private static ParentRepo parentRepo;

    public static synchronized ParentRepo getInstance() {
        if (parentRepo == null) {
            parentRepo = new ParentRepo();
        }
        return parentRepo;
    }

    private MutableLiveData<List<ChildModel>> childMutableLiveData = new MutableLiveData<>();

    public void registerParent(final Context context, String email, String password) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FirebaseEnums.PARENTS.getValue()).child(email);
        ParentModel parentModel = new ParentModel();
        parentModel.setEmail(email);
        parentModel.setPwd(password);
        AppUtils.getInstance().showpDialog(context);
        myRef.setValue(parentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public LiveData<List<ChildModel>> loadChildrenLocation(final Application application, String email) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(FirebaseEnums.PARENTS.getValue())
                .child(email);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ParentModel parentModel = dataSnapshot.getValue(ParentModel.class);
                if (parentModel == null || parentModel.getChildEmail() == null) {
                    Toast.makeText(application, "No Child Found" +
                            ", Please add some Child", Toast.LENGTH_SHORT).show();
                    AppUtils.getInstance().hidepDialog();
                    return;
                }
                getLocations(application, parentModel.getChildEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(application, "Unable to update Child Location" +
                        ", Please try again Later", Toast.LENGTH_SHORT).show();
                AppUtils.getInstance().hidepDialog();
            }
        });

        return childMutableLiveData;
    }

    private void getLocations(final Application application, final List<String> childUsernames) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference(FirebaseEnums.CHILDREN.getValue());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<ChildModel> parentChildModels = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChildModel childModel = snapshot.getValue(ChildModel.class);
                    if (childUsernames.contains(childModel.getEmail())) {
                        parentChildModels.add(childModel);
                    }
                }
                childMutableLiveData.setValue(parentChildModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(application, "Unable to update Child Location" +
                        ", Please try again Later", Toast.LENGTH_SHORT).show();
                AppUtils.getInstance().hidepDialog();
            }
        });
    }

    public void addChildForParent(final Application application, ParentModel parentModel, String childEmail) {
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference myRef = firebaseDatabase.getReference(FirebaseEnums.PARENTS.getValue())
                .child(parentModel.getEmail());
        List<String> childEmails;
        if (parentModel.getChildEmail() != null) {
            childEmails = parentModel.getChildEmail();
        } else {
            childEmails = new ArrayList<>();
        }
        childEmails.add(AppUtils.getInstance().getEmailWithoutdot(childEmail));
        parentModel.setChildEmail(childEmails);
        myRef.setValue(parentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(application, "New Child has been Added"
                        , Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application
                        , "Unable to add, please try Again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
