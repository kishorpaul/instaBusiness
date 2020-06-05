package com.example.itravel.App2.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.itravel.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSignUpActivity extends AppCompatActivity {
    private Button loginLink,regBtn;
    private TextInputLayout uName, fName, eMail, pAssword, pHone;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    ProgressDialog dialog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_sign_up);

        loginLink = findViewById(R.id.already_user_btn);
        uName = findViewById(R.id.reg_userName);
        fName = findViewById(R.id.reg_fullName);
        pAssword = findViewById(R.id.reg_password);
        pHone = findViewById(R.id.reg_phone);
        eMail = findViewById(R.id.reg_email);
        regBtn = findViewById(R.id.reg_btn);

        mAuth = FirebaseAuth.getInstance();

        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSignUpActivity.this, UserLoginActivity.class));
                finish();
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regUser(uName,fName,eMail,pHone,pAssword);
            }
        });
    }
    public  void regUser(TextInputLayout uname, TextInputLayout fName, TextInputLayout email, TextInputLayout phone, TextInputLayout password){
        String nametxt = uname.getEditText().getText().toString();
        String fnametxt = fName.getEditText().getText().toString();
        String emailtxt = email.getEditText().getText().toString();
        String passtxt = password.getEditText().getText().toString();
        String phtxt = phone.getEditText().getText().toString();

        if (nametxt.isEmpty()){
            uName.setError("Field id required");
        }else if (fnametxt.isEmpty()){
            uName.setError("Field id required");
        }else if (emailtxt.isEmpty()){
            eMail.setError("Field id required");
        }else if (passtxt.isEmpty() && passtxt.length()<8){
            pAssword.setError("Field id required");
        }else if (phtxt.isEmpty() && (phtxt.length()<10)||phtxt.length()>10){
            pHone.setError("Field id required");
        }else{
            savetoDB(nametxt,fnametxt,emailtxt,passtxt,phtxt);
        }
    }

    private void savetoDB(final String nametxt, final String fnametxt, final String emailtxt, final String passtxt, final String phtxt) {
        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setContentView(R.layout.loading_loading);
        dialog.setCanceledOnTouchOutside(false);

        dbRef.child("+91"+phtxt).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(UserSignUpActivity.this, "User with phone number already exists", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else{
                    Intent i = new Intent(getApplicationContext(), UserVerifyPhoneActivity.class);
                    i.putExtra("phone", phtxt);
                    i.putExtra("uName", nametxt);
                    i.putExtra("fName", fnametxt);
                    i.putExtra("password", passtxt);
                    i.putExtra("email", emailtxt);
                    startActivity(i);
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
      //  Users users = new Users(nametxt, fnametxt, emailtxt, passtxt, phtxt);

//        HashMap<String, Object> userMap = new HashMap<>();
//        userMap.put("phone",phtxt);
//        userMap.put("email",emailtxt);
//        userMap.put("password",passtxt);
//        userMap.put("uName",nametxt);
//        userMap.put("fName",fnametxt);
//
//        dbRef.child(phtxt).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(SignUpActivity.this, "Successful", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                    startActivity(new Intent(SignUpActivity.this, VerifyPhoneActivity.class));
//                    finish();
//                }else{
//                    Toast.makeText(SignUpActivity.this, "Error:"+task.getException().toString(), Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                }
//            }
//        });
    }
}

