package com.example.itravel.App2.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itravel.App2.Common.DashBoardActivity;
import com.example.itravel.App2.Prevalent.Prevalent;
import com.example.itravel.App1.Models.Users;
import com.example.itravel.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class UserLoginActivity extends AppCompatActivity {
 private Button  regLink,loginBtn,login_forgotPass;
 ImageView logoImage;
 TextView t1, t2;
 TextInputLayout phone, password;
 private ProgressDialog dialog;
 private CardView loginGoogle;
 FirebaseAuth mAuth;
 CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_login);

        regLink = findViewById(R.id.new_user_btn);
        phone= findViewById(R.id.login_phone);
        password = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        logoImage = findViewById(R.id.login_image);
        checkBox = findViewById(R.id.login_remember_me);
        login_forgotPass = findViewById(R.id.login_forgotPass);

        login_forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserLoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        Paper.init(this);
//        t1 = findViewById(R.id.login_name);
//        t2 = findViewById(R.id.login_title);
        //loginGoogle = findViewById(R.id.login_google);

//        loginGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(UserLoginActivity.this, GoogleAuthActivity.class));
//                finish();
//            }
//        });
        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uNametxt = phone.getEditText().getText().toString();
                String passtxt = password.getEditText().getText().toString();

                if (uNametxt.isEmpty()){
                    phone.setError("Field Required");
                }else if(passtxt.isEmpty()){
                    password.setError("Field Required");
                }else{
                    isuser(uNametxt,passtxt);
                }
            }
        });

        regLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(UserLoginActivity.this, UserSignUpActivity.class);

//               Pair[] pairs = new Pair[7];
//               pairs[0] = new Pair<View, String>(logoImage, "logoImage");
//               pairs[1] = new Pair<View, String>(t1, "logoText");
//               pairs[2] = new Pair<View, String>(t2, "logoT2");
//               pairs[3] = new Pair<View, String>(phone, "logoe1");
//               pairs[4] = new Pair<View, String>(password, "logoe2");
//               pairs[5] = new Pair<View, String>(loginBtn, "logob1");
//               pairs[6] = new Pair<View, String>(regLink, "logob2");
//
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserLoginActivity.this, pairs);
//                    startActivity(i, options.toBundle());
//                    finish();
//                }else{
                    startActivity(i);
                    finish();
                //}
            }
        });

        String phoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String passwordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (phoneKey != "" && passwordKey != "") {
            if (!TextUtils.isEmpty(phoneKey) && !TextUtils.isEmpty(passwordKey)){
                AllowAccess(phoneKey,passwordKey);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(UserLoginActivity.this, DashBoardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    private void isuser(final String uNametxt, final String passtxt) {
        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setContentView(R.layout.loading_loading);
        dialog.setCanceledOnTouchOutside(false);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");

        Query cUser = dbRef.orderByChild("phone").equalTo("+91"+uNametxt);

        cUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    phone.setError(null);
                    phone.setErrorEnabled(false);
                    String pass = dataSnapshot.child("+91"+uNametxt).child("password").getValue().toString();
                    if (pass.equals(passtxt)){
                        password.setError(null);
                        password.setErrorEnabled(false);

                        if (checkBox.isChecked()){
                            Paper.book().write(Prevalent.UserPhoneKey, uNametxt);
                            Paper.book().write(Prevalent.UserPasswordKey, passtxt);
                        }

                        Intent i = new Intent(UserLoginActivity.this, UserProfileActivity.class);
                        i.putExtra("phone", uNametxt);
                        i.putExtra("arrive", "login");
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(i);
                        finish();
                        dialog.dismiss();

                    }else{
                        password.setError("Wrong password");
                        password.requestFocus();
                        dialog.dismiss();
                        //Toast.makeText(LoginActivity.this, "Password is wrong", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    phone.setError("No user found");
                    phone.requestFocus();
                    dialog.dismiss();
                    //Toast.makeText(LoginActivity.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
//    @Override
////    protected void onStart() {
////        super.onStart();
////        if (mAuth!=null){
////            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
////            if(user!=null){
////                dialog = new ProgressDialog(this);
////                dialog.show();
////                dialog.setContentView(R.layout.loading_please_wait);
////                dialog.setCanceledOnTouchOutside(false);
////
////                Intent i= new Intent(UserLoginActivity.this, UserProfileActivity.class);;
////                startActivity(i);
////                dialog.dismiss();
////            }
////        }
////    }
    private void AllowAccess(final String userPhKey, final String userPassKey) {
        dialog = new ProgressDialog(UserLoginActivity.this);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.lodaing_dialog_loading);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child("+91"+userPhKey).exists()){
                    Users userData = dataSnapshot.child("Users").child("+91"+userPhKey).getValue(Users.class);
                    if (userData.getPhone().equals("+91"+userPhKey)){
                        if (userData.getPassword().equals(userPassKey)){
                            Intent i = new Intent(UserLoginActivity.this, UserProfileActivity.class);
                            Prevalent.currentOnlineUser = userData;
                            i.putExtra("phone", userPhKey);
                            i.putExtra("arrive", "login");
                            startActivity(i);
                            finish();
                            Toast.makeText(UserLoginActivity.this, "Logged in Again", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }else{
                        Toast.makeText(UserLoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
