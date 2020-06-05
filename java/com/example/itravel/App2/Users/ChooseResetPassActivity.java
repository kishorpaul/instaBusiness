package com.example.itravel.App2.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.itravel.R;

public class ChooseResetPassActivity extends AppCompatActivity {
    String phone,email;
    private TextView phDis,emailDis;
    private Button choose_reset_phBtn,choose_reset_emailBtn;
    private ProgressDialog dialog;
    private ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_reset_pass);

        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");

        phDis = findViewById(R.id.choose_reset_phone);
        choose_reset_phBtn = findViewById(R.id.choose_reset_phBtn);
        choose_reset_emailBtn = findViewById(R.id.choose_reset_emailBtn);
        emailDis = findViewById(R.id.choose_reset_email);
        close = findViewById(R.id.choose_reset_close);

        phDis.setText("on "+"+91 "+phone);
        emailDis.setText("on "+email);

        choose_reset_emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailLink(email);
            }
        });

        choose_reset_phBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp(phone);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseResetPassActivity.super.onBackPressed();
            }
        });
    }

    private void sendOtp(String phone) {
        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setContentView(R.layout.loading_pg_bar);
        dialog.setCanceledOnTouchOutside(false);
        Intent i = new Intent(ChooseResetPassActivity.this, ResetPassPhOtpActivity.class);
        i.putExtra("phone", phone);
        startActivity(i);
    }

    private void sendEmailLink(String email) {
        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setContentView(R.layout.loading_pg_bar);
        dialog.setCanceledOnTouchOutside(false);
    }
}
