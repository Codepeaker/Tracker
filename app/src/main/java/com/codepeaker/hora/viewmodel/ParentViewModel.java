package com.codepeaker.hora.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.codepeaker.hora.model.ChildModel;
import com.codepeaker.hora.model.ParentModel;
import com.codepeaker.hora.repo.ParentRepo;

import java.util.List;

public class ParentViewModel extends AndroidViewModel {

    private LiveData<List<ChildModel>> childLiveData;

    public ParentViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadChildrenLocation(String email) {
        childLiveData = ParentRepo.getInstance().loadChildrenLocation(getApplication(),email);
    }

    public LiveData<List<ChildModel>> getChildLiveData() {
        return childLiveData;
    }


    public void addChildForParent(ParentModel parentModel, String childUsername) {
        ParentRepo.getInstance().addChildForParent(getApplication(), parentModel, childUsername);
    }
}
