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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class MerchantSignUpActivity extends AppCompatActivity {
    ImageView back;
    TextInputLayout phone, fName, email, password;
    Button callLogin, regBtn;
    ProgressDialog loadingdialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_merchant_sign_up);

        back = findViewById(R.id.merch_reg_back);
        fName = findViewById(R.id.merch_reg_fullName);
        email = findViewById(R.id.merch_reg_email);
        phone = findViewById(R.id.merch_reg_phone);
        password = findViewById(R.id.merch_reg_password);
        callLogin = findViewById(R.id.merch_login_calllogin);
        regBtn = findViewById(R.id.merch_reg_nextBtn);

        mAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MerchantSignUpActivity.super.onBackPressed();
            }
        });

        callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MerchantSignUpActivity.this, MerchantLoginActivity.class));
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });
    }

    private void registerSeller() {
        final String txtName = fName.getEditText().getText().toString();
        final String txtphone = phone.getEditText().getText().toString();
        final String txtemail = email.getEditText().getText().toString();
        final String txtpass = password.getEditText().getText().toString();

        loadingdialog = new ProgressDialog(this);
        loadingdialog.show();
        loadingdialog.setContentView(R.layout.loading_please_wait);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if (!txtemail.equals("") && !txtName.equals("") && !txtphone.equals("") && !txtpass.equals("")){
            mAuth.createUserWithEmailAndPassword(txtemail, txtpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        final DatabaseReference selRef = FirebaseDatabase.getInstance().getReference().child("Sellers");
                        final String sid = mAuth.getCurrentUser().getUid();
                        final String deviceToken = FirebaseInstanceId.getInstance().getToken();

                        HashMap<String, Object> selMap = new HashMap<>();
                        selMap.put("uid", sid);
                        selMap.put("name", txtName);
                        selMap.put("email",txtemail);
                        selMap.put("password", txtpass);
                        selMap.put("phone", txtphone);

                        selRef.child(sid).updateChildren(selMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                selRef.child(sid).child("deviceToken").setValue(deviceToken).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            loadingdialog.dismiss();
                                            Intent i= new Intent(MerchantSignUpActivity.this, SellerHomeActivity.class);;
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i);
                                            finish();
                                            Toast.makeText(MerchantSignUpActivity.this, "Seller account created", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }else{
            loadingdialog.dismiss();
            Toast.makeText(this, "Please provide all the above details", Toast.LENGTH_SHORT).show();
        }
    }
//
//    public void callNextScreen(View view){
//        Intent i = new Intent(MerchantSignUpActivity.this, MerchSignUp2Activity.class);
//        startActivity(i);
//    }
}
