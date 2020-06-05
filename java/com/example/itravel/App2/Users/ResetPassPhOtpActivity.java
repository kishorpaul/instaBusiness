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
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.itravel.App2.Common.DashBoardActivity;
import com.example.itravel.App2.Sellers.SellerHomeActivity;
import com.example.itravel.R;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ResetPassPhOtpActivity extends AppCompatActivity {
    ImageView close;
    PinView code;
    Button verBtn;
    TextView errorText;
    String phone, verificationCode;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reset_pass_ph_otp);

        close = findViewById(R.id.reset_pass_phClose);
        verBtn = findViewById(R.id.reset_pass_phVerBtn);
        code = findViewById(R.id.reset_pass_phCode);
        errorText = findViewById(R.id.errorText);

        phone = getIntent().getStringExtra("phone");

        sendOTP(phone);

        verBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code.getText().equals("") || code.length() < 6){
                    //Toast.makeText(ResetPassPhOtpActivity.this, "Please enter the correct code sent", Toast.LENGTH_SHORT).show();
                    code.setError("Please enter the correct code sent");
                }else{
                    verifyManualCode(code);
                    Toast.makeText(ResetPassPhOtpActivity.this, verificationCode, Toast.LENGTH_SHORT).show();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPassPhOtpActivity.this, UserLoginActivity.class));
                finish();
            }
        });
    }

    private void verifyManualCode(PinView code) {
        String code1 = code.getText().toString();
        if (code1.equals(verificationCode)){
            dialog = new ProgressDialog(ResetPassPhOtpActivity.this);
            dialog.show();
            dialog.setContentView(R.layout.lodaing_dialog_loading);
            dialog.setCanceledOnTouchOutside(false);
           // PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, code);
            Intent i = new Intent(ResetPassPhOtpActivity.this, SetNewPassActivity.class);
            i.putExtra("phone", phone);
            startActivity(i);
            dialog.dismiss();
        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    private void sendOTP(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phone,
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
            verificationCode = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code!=null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(ResetPassPhOtpActivity.this, "Error: "+e.toString(), Toast.LENGTH_LONG).show();
            errorText.setText(e.getMessage().toString());
            //t1.setText(e.toString());
        }
    };

    private void verifyCode(String code) {
        dialog = new ProgressDialog(ResetPassPhOtpActivity.this);
        dialog.show();
        dialog.setContentView(R.layout.lodaing_dialog_loading);
        dialog.setCanceledOnTouchOutside(false);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, code);
        Intent i = new Intent(ResetPassPhOtpActivity.this, SetNewPassActivity.class);
        i.putExtra("phone", phone);
        startActivity(i);
        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ResetPassPhOtpActivity.this, DashBoardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

}
