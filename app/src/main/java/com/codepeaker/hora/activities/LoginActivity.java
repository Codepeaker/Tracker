package com.codepeaker.hora.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.codepeaker.hora.R;
import com.codepeaker.hora.databinding.ActivityLoginBinding;
import com.codepeaker.hora.databinding.ContentLoginBinding;
import com.codepeaker.hora.enums.FirebaseEnums;
import com.codepeaker.hora.model.ChildModel;
import com.codepeaker.hora.model.ParentModel;
import com.codepeaker.hora.utils.AppUtils;
import com.codepeaker.hora.viewmodel.LoginViewModel;

import java.util.ArrayList;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ContentLoginBinding contentLoginBinding;
    private String TAG = "adsfa";
    private FirebaseEnums selectedEnum;
    private LoginViewModel loginViewModel;
    private Observer<Object> objectObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setSupportActionBar(binding.toolbar);

        loginViewModel = ViewModelProviders.of(LoginActivity.this)
                .get(LoginViewModel.class);

        initObserver();

        contentLoginBinding = binding.contentLoginId;

        contentLoginBinding.signInBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = contentLoginBinding.emailEt.getText().toString().trim();
                String pass = contentLoginBinding.passwordEt.getText().toString().trim();
                if (!AppUtils.getInstance().isValidFields(LoginActivity.this, email, pass)) {
                    return;
                }
                AppUtils.getInstance().showpDialog(LoginActivity.this);
                if (contentLoginBinding.typeRg.getCheckedRadioButtonId() == R.id.parent_rb) {
                    selectedEnum = FirebaseEnums.PARENTS;
                    loginViewModel.loadUserData(selectedEnum, pass, email);
                    loginViewModel.getUser().observe(LoginActivity.this, objectObserver);
                } else if (contentLoginBinding.typeRg.getCheckedRadioButtonId() == R.id.child_rb) {
                    selectedEnum = FirebaseEnums.CHILDREN;
                    loginViewModel.loadUserData(selectedEnum, pass, email);
                    loginViewModel.getUser().observe(LoginActivity.this, objectObserver);
                } else {
                    Toast.makeText(LoginActivity.this, "Please Select parent or child"
                            , Toast.LENGTH_SHORT).show();
                    AppUtils.getInstance().hidepDialog();
                }
            }
        });


    }

    private void initObserver() {
        objectObserver = new Observer<Object>() {
            @Override
            public void onChanged(@Nullable Object object) {
                Intent intent;
                if (FirebaseEnums.PARENTS.getValue().equals(selectedEnum.getValue())) {
                    ParentModel parentModel = getParentModel((Map<String, Object>) object);
                    intent = new Intent(LoginActivity.this, ParentActivity.class);
                    intent.putExtra("parent", parentModel);
                } else {
                    ChildModel childModel = getChildModel((Map<String, Object>) object);
                    intent = new Intent(LoginActivity.this, ChildActivity.class);
                    intent.putExtra("child", childModel);
                }
                startActivity(intent);
            }
        };
    }


    private ChildModel getChildModel(Map<String, Object> td) {
        ChildModel childModel = new ChildModel();
        if (td.get("email") != null) {
            childModel.setEmail(td.get("email").toString());
        }

        if (td.get("pwd") != null) {
            childModel.setPwd(td.get("pwd").toString());
        }

        if (td.get("location") != null) {
            childModel.setLocation((td.get("location").toString()));
        }
        return childModel;
    }

    private ParentModel getParentModel(Map<String, Object> td) {
        ParentModel parentModel = new ParentModel();
        if (td.get("email") != null) {
            parentModel.setEmail(td.get("email").toString());
        }

        if (td.get("pwd") != null) {
            parentModel.setPwd(td.get("pwd").toString());
        }

        if (td.get("childEmail") != null) {
            parentModel.setChildEmail(((ArrayList<String>) td.get("childEmail")));
        }
        return parentModel;
    }

    public void onRegister(View view) {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
