package com.example.itravel.App2.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.itravel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Button nextBtn;
    private EditText input;
    private ImageView close;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);

        nextBtn = findViewById(R.id.forgotPass_next);
        input = findViewById(R.id.forgotPass_phone);
        close = findViewById(R.id.forgotPass_close);
        progressBar = findViewById(R.id.forgotPass_pb);

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                final String phone = input.getText().toString();

                dbRef.child("+91"+phone).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String email = dataSnapshot.child("email").getValue().toString();
                            Intent i = new Intent(ForgotPasswordActivity.this, ChooseResetPassActivity.class);
                            i.putExtra("phone", phone);
                            i.putExtra("email", email);
                            startActivity(i);
                            progressBar.setVisibility(View.INVISIBLE);
                            nextBtn.setVisibility(View.VISIBLE);
                        }else{
                            Toast.makeText(ForgotPasswordActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            nextBtn.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordActivity.super.onBackPressed();
            }
        });
    }
}
