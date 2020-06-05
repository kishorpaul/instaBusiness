package com.example.itravel.App2.Admin;

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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity {
    private Button adminLogin;
    private TextInputLayout phone, pass;
    private ImageView back;
    private DatabaseReference dbRef;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_login);

        back = findViewById(R.id.admin_back);
        phone = findViewById(R.id.admin_phone);
        pass = findViewById(R.id.admin_pass);
        adminLogin = findViewById(R.id.admin_login_btn);

        dbRef = FirebaseDatabase.getInstance().getReference().child("Admin");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminLoginActivity.super.onBackPressed();
            }
        });

        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(AdminLoginActivity.this, "Phone number shouldn't be empty", Toast.LENGTH_SHORT).show();
                }else if(pass.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(AdminLoginActivity.this, "Password shouldn't be empty", Toast.LENGTH_SHORT).show();
                }else {
                    loginAdmin();
                }
            }
        });
    }

    private void loginAdmin() {
        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setContentView(R.layout.lodaing_dialog_loading);
        dialog.setCanceledOnTouchOutside(false);

        String phtxt = phone.getEditText().getText().toString();
        final String passtxt = pass.getEditText().getText().toString();

        dbRef.child(phtxt).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String pass1 = dataSnapshot.child("pass").getValue().toString();
                    if (pass1.equals(passtxt)){
                        startActivity(new Intent(AdminLoginActivity.this, AdminHomeActivity.class));
                        finish();
                        dialog.dismiss();
                    }else{
                        Toast.makeText(AdminLoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }else{
                    Toast.makeText(AdminLoginActivity.this, "Wrong Phone Number", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
