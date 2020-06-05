package com.example.itravel.App2.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.itravel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class UserVerifyPhoneActivity extends AppCompatActivity {
    private String uNametxt,fNametxt,passtxt,phtxt,emailtxt;
    private Button verifyBtn;
    private ProgressBar progressBar;
    private PinView inputEdit;
    ImageView userVerifyPhClose;
    TextView t1;
    private String verificationCodeBySystem;
    DatabaseReference dbRef;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_phone);

        //progressBar = findViewById(R.id.ver);
        verifyBtn = findViewById(R.id.verify_otp_btn);
        inputEdit = findViewById(R.id.verify_otp);
        userVerifyPhClose = findViewById(R.id.userVerifyPhClose);
        t1 = findViewById(R.id.t4);

        Intent i = getIntent();
        uNametxt = i.getStringExtra("uName");
        passtxt = i.getStringExtra("password");
        emailtxt = i.getStringExtra("email");
        phtxt = i.getStringExtra("phone");
        fNametxt = i.getStringExtra("fName");

        userVerifyPhClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserVerifyPhoneActivity.this, UserLoginActivity.class));
                finish();
            }
        });

        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");

        sendOtp(phtxt);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = inputEdit.getText().toString();
                if (code.isEmpty() || code.length()<6){
                    inputEdit.setError("Code is not correct");
                    inputEdit.requestFocus();
                    return;
                }
//                progressBar.setVisibility(View.VISIBLE);
  //              verifyBtn.setVisibility(View.INVISIBLE);
                verifyCode(code);
            }
        });

    }

    private void sendOtp(String phtxt) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phtxt,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBacks
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code!=null){
               // progressBar.setVisibility(View.VISIBLE);
               // verifyBtn.setVisibility(View.INVISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(UserVerifyPhoneActivity.this, "Error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
            t1.setText(e.getMessage().toString());
        }
    };

    private void verifyCode(String vCode) {
       PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, vCode);
       signinByCredentials(credential);
    }

    private void signinByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setContentView(R.layout.lodaing_dialog_loading);
        dialog.setCanceledOnTouchOutside(false);

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
//                HashMap<String, Object> userMap = new HashMap<>();
//                userMap.put("phone","+91"+phtxt);
//                userMap.put("email",emailtxt);
//                userMap.put("password",passtxt);
//                userMap.put("uName",uNametxt);
//                userMap.put("fName",fNametxt);
//                userMap.put("imageUrl","default");
//
//                dbRef.child(phtxt).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(VerifyPhoneActivity.this, "Successful", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                            startActivity(new Intent(VerifyPhoneActivity.this, VerifyPhoneActivity.class));
//                            finish();
//                        }else{
//                            Toast.makeText(VerifyPhoneActivity.this, "Error:"+task.getException().toString(), Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        }
//                    }
//                });
                    Intent i = new Intent(UserVerifyPhoneActivity.this, UpdateUserProfileActivity.class);
                    i.putExtra("password", passtxt);
                    i.putExtra("uName", uNametxt);
                    i.putExtra("fName", fNametxt);
                    i.putExtra("email", emailtxt);
                    i.putExtra("phone", phtxt);
                    startActivity(i);
                    dialog.dismiss();
                    finish();
                }else{
                    Toast.makeText(UserVerifyPhoneActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });
    }
}
