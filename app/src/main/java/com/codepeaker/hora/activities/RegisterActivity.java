package com.codepeaker.hora.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.codepeaker.hora.R;
import com.codepeaker.hora.databinding.ActivityRegisterBinding;
import com.codepeaker.hora.databinding.ContentRegisterBinding;
import com.codepeaker.hora.repo.ChildRepo;
import com.codepeaker.hora.repo.ParentRepo;
import com.codepeaker.hora.utils.AppUtils;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    int type;
    ContentRegisterBinding contentRegisterBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        setSupportActionBar(binding.toolbar);
        contentRegisterBinding = binding.contentId;

        contentRegisterBinding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = contentRegisterBinding.emailEt.getText().toString().trim();
                String pass = contentRegisterBinding.passwordEt.getText().toString().trim();
                if (!AppUtils.getInstance().isValidFields(RegisterActivity.this, email, pass)) {
                    return;
                }
                email = AppUtils.getInstance().getEmailWithoutdot(email);
                if (contentRegisterBinding.typeRg.getCheckedRadioButtonId() == R.id.parent_rb) {
                    ParentRepo.getInstance().registerParent(RegisterActivity.this, email, pass);
                } else if (contentRegisterBinding.typeRg.getCheckedRadioButtonId() == R.id.child_rb) {
                    ChildRepo.getInstance().registerChild(RegisterActivity.this, email, pass);
                } else {
                    Toast.makeText(RegisterActivity.this, "Please Select parent or child"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void onLogin(View view) {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
