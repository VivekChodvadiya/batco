package com.softfinite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.softfinite.utils.RequestParamsUtils;
import com.softfinite.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

//import com.afollestad.materialdialogs.DialogAction;
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.kartum.utils.Constant;

public class PasswordActivity extends BaseActivity {

    @BindView(R.id.editPassword)
    EditText editPassword;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    String password = "batcoroadlines";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editPassword.getText().toString().trim().equalsIgnoreCase(password)) {
                    Intent i = new Intent(getActivity(), HomeActivity.class);
                    startActivity(i);
                    finishAffinity();
                    Utils.setPref(getActivity(), RequestParamsUtils.USER_ID, "true");
                    showToast("Login Success", Toast.LENGTH_SHORT);
                } else {
                    showToast("You have entered an invalid password", Toast.LENGTH_SHORT);
                }
            }
        });
    }
}
