package com.example.itravel.App2.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.itravel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import io.paperdb.Paper;

public class UserUpdateEditedProfileActivity extends AppCompatActivity {
    ImageView close;
    EditText uName, fName, email;
    Button update;
    String nametxt, fnametxt, emailtxt, phtxt,passtxt,curPh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_update_edited_profile);

        close = findViewById(R.id.uuepClose);
        uName = findViewById(R.id.uuepUname);
        fName = findViewById(R.id.uuepfName);
        email = findViewById(R.id.uuepEmail);
        update = findViewById(R.id.uuepBtn);

        Intent i = getIntent();
        nametxt = i.getStringExtra("uName");
        fnametxt = i.getStringExtra("fName");
        emailtxt = i.getStringExtra("email");
        phtxt = i.getStringExtra("phone");
        passtxt = i.getStringExtra("password");

        uName.setText(nametxt);
        fName.setText(fnametxt);
        email.setText(emailtxt);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserUpdateEditedProfileActivity.super.onBackPressed();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName1 = uName.getText().toString();
                String fName1 = fName.getText().toString();
                String email1 = email.getText().toString();

                if (uName1.equals("") || fName1.equals("")||email1.equals("")){
                    Toast.makeText(UserUpdateEditedProfileActivity.this, "Please provide the credentials", Toast.LENGTH_SHORT).show();
                }else if (!email1.contains("@gmail.com")){
                    Toast.makeText(UserUpdateEditedProfileActivity.this, "Provided email id is wrong", Toast.LENGTH_SHORT).show();
                }else{
                    updateDatabase(uName1, fName1,email1);
                }
            }
        });
    }

    private void updateDatabase(String uName1, String fName1, String email1) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setContentView(R.layout.loading_please_wait);
        dialog.setCanceledOnTouchOutside(false);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("uName", uName1);
        userMap.put("fName", fName1);
        userMap.put("email", email1);

        dbRef.child(phtxt).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(UserUpdateEditedProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    UserUpdateEditedProfileActivity.super.onBackPressed();
                    dialog.dismiss();
                }
            }
        });
    }
}
