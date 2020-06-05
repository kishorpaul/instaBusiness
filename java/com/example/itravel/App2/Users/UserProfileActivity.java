package com.example.itravel.App2.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.itravel.App2.Common.DashBoardActivity;
import com.example.itravel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class UserProfileActivity extends AppCompatActivity {
    private TextView username, phone, fullName, email,userProfile_edit;
    CircleImageView profileImage;
    ProgressDialog dialog;
    private DatabaseReference dbRef;
    FirebaseAuth mAuth;
    ImageView cancel, logout;
    String phoneRec="", arrive="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);

        email = findViewById(R.id.userProfile_email);
        fullName = findViewById(R.id.userProfile_fullName);
        username = findViewById(R.id.userProfile_userName);
        phone = findViewById(R.id.userProfile_phone);
        profileImage = findViewById(R.id.userProfile_image);
        cancel = findViewById(R.id.userProfile_cancel);
        logout = findViewById(R.id.userProfile_logout);
        userProfile_edit = findViewById(R.id.userProfile_edit);

        Paper.init(this);

        phoneRec = getIntent().getStringExtra("phone");
        arrive = getIntent().getStringExtra("arrive");

        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mAuth.signOut();
                Paper.book().destroy();
                Intent i = new Intent(UserProfileActivity.this, DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setContentView(R.layout.lodaing_dialog_loading);
        dialog.setCanceledOnTouchOutside(false);

      //  if (arrive.equals("signUp")) {

//            String uid = mAuth.getCurrentUser().getPhoneNumber();
//            Query cUser = dbRef.orderByChild("uid").equalTo(uid);
//
//            Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();
//
//            dbRef.child(uid).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        String imageUrl1 = dataSnapshot.child("imageUrl").getValue().toString();
//                        String uName = dataSnapshot.child("uName").getValue().toString();
//                        String fName = dataSnapshot.child("fName").getValue().toString();
//                        String phone1 = dataSnapshot.child("phone").getValue().toString();
//                        String email1 = dataSnapshot.child("email").getValue().toString();
//
//                        if (dataSnapshot.child("imageUrl").equals("default")) {
//                            username.setText(uName);
//                            fullName.setText(fName);
//                            email.setText(email1);
//                            phone.setText(phone1);
//                            dialog.dismiss();
//                        } else {
//                            username.setText(uName);
//                            fullName.setText(fName);
//                            email.setText(email1);
//                            phone.setText(phone1);
//
//                            Glide.with(UserProfileActivity.this).load(imageUrl1).into(profileImage);
//                            dialog.dismiss();
//                        }
//
//                    } else {
//                        Toast.makeText(UserProfileActivity.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
      //  else if (arrive.equals("login")){
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");

           // Query cUser = dbRef.orderByChild("phone").equalTo("+91"+phoneRec);

            dbRef.child("+91"+phoneRec).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String imageUrl1 = dataSnapshot.child("imageUrl").getValue().toString();
                        final String uName = dataSnapshot.child("uName").getValue().toString();
                        final String fName = dataSnapshot.child("fName").getValue().toString();
                        final String phone1 = dataSnapshot.child("phone").getValue().toString();
                        final String email1 = dataSnapshot.child("email").getValue().toString();
                        final String pass1 = dataSnapshot.child("password").getValue().toString();

                        userProfile_edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(UserProfileActivity.this, UserUpdateEditedProfileActivity.class);
                               // i.putExtra("arrive", "editProfile");
                                i.putExtra("uName", uName);
                                i.putExtra("fName", fName);
                                i.putExtra("email", email1);
                                i.putExtra("phone", phone1);
                                i.putExtra("password", pass1);
                                startActivity(i);
                            }
                        });

                        if (dataSnapshot.child("imageUrl").equals("default")) {
                            username.setText(uName);
                            fullName.setText(fName);
                            email.setText(email1);
                            phone.setText(phone1);
                            dialog.dismiss();
                        } else {
                            username.setText(uName);
                            fullName.setText(fName);
                            email.setText(email1);
                            phone.setText(phone1);

                            Glide.with(UserProfileActivity.this).load(imageUrl1).into(profileImage);
                            dialog.dismiss();
                        }

                    }else{
                        Toast.makeText(UserProfileActivity.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }
}
