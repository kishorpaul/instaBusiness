package com.example.itravel.App2.LoginSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.itravel.R;

public class MerchSignUp2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_merch_sign_up2);
    }

    public void callNextScreen(View view){
        Intent i = new Intent(MerchSignUp2Activity.this, MerchantSignUp3activity.class);
        startActivity(i);
    }
}
