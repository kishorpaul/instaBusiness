
package com.example.itravel.App2.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.itravel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MerchantLoginActivity extends AppCompatActivity {
    private TextInputLayout email, password;
    private Button loginBtn, regLink;
    private ProgressDialog loadingdialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_merchant_login);

        regLink = findViewById(R.id.merch_login_callReg);
        password = findViewById(R.id.merch_login_password);
        email = findViewById(R.id.merch_login_emai);
        loginBtn = findViewById(R.id.merch_login_btn);
        ImageView back = findViewById(R.id.merch_login_back);

        mAuth = FirebaseAuth.getInstance();

        regLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MerchantLoginActivity.this, MerchantSignUpActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MerchantLoginActivity.super.onBackPressed();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSeller();
            }
        });
    }

    private void loginSeller() {
        loadingdialog = new ProgressDialog(this);
        loadingdialog.show();
        loadingdialog.setContentView(R.layout.loading_please_wait);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String txtEmail = email.getEditText().getText().toString();
        String txtpass = password.getEditText().getText().toString();

        if (txtEmail.equals("")) {
            loadingdialog.dismiss();
            Toast.makeText(MerchantLoginActivity.this, "Please enter the fields", Toast.LENGTH_SHORT).show();
        }else if(txtpass.equals("")){
            loadingdialog.dismiss();
            Toast.makeText(MerchantLoginActivity.this, "Please enter the fields", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(txtEmail, txtpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent i = new Intent(MerchantLoginActivity.this, SellerHomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    } else {
                        loadingdialog.dismiss();
                        Toast.makeText(MerchantLoginActivity.this, "Error :" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
