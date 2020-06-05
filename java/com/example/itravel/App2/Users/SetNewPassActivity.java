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
import android.widget.Toast;

import com.example.itravel.App2.Common.DashBoardActivity;
import com.example.itravel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPassActivity extends AppCompatActivity {
    TextInputLayout pass, pass1;
    Button setPassBtn;
    String phone;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_new_pass);

        phone = getIntent().getStringExtra("phone");
        pass = findViewById(R.id.newPassword);
        pass1 = findViewById(R.id.repeatNewPassword);
        setPassBtn = findViewById(R.id.passSetBtn);

        findViewById(R.id.set_password_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SetNewPassActivity.this, UserLoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        setPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass1txt = pass.getEditText().getText().toString();
                String pass2txt = pass1.getEditText().getText().toString();

                if (pass1txt.equals("") || pass1txt.length()<8){
                    pass.getEditText().setError("Please provide a strong password");
                }else if(!pass1txt.equals(pass2txt)){
                    pass1.getEditText().setError("Password should match");
                }else{
                    updateDB(phone, pass);
                }
            }
        });
    }

    private void updateDB(String phone, TextInputLayout pass) {
        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setContentView(R.layout.loading_please_wait);
        dialog.setCanceledOnTouchOutside(false);

        String pass1 = pass.getEditText().getText().toString();

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child("+91"+phone);

        dbRef.child("password").setValue(pass1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(SetNewPassActivity.this, ForgotPassSuccessActivity.class));
                    finish();
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(SetNewPassActivity.this, "Database Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SetNewPassActivity.this, UserLoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
