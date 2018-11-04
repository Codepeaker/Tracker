package com.codepeaker.hora.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.codepeaker.hora.enums.FirebaseEnums;
import com.codepeaker.hora.repo.CommonRepo;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<Object> mapLiveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadUserData(FirebaseEnums firebaseEnums, String pwd, String email) {
        mapLiveData = CommonRepo.getInstance().signInUser(getApplication(), firebaseEnums, pwd
                                                                                        , email);
    }

    public MutableLiveData<Object> getUser() {
        return mapLiveData;
    }

}
