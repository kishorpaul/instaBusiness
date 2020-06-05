package com.example.itravel.App2.LoginSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.itravel.R;

public class MerchantSignUp3activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_merchant_sign_up3activity);
    }


    public void callNext(View view){
        Intent i = new Intent(MerchantSignUp3activity.this, MerhantVerifyOTPActivity.class);
        startActivity(i);
    }
}
