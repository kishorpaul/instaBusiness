package com.example.itravel.App2.Users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.itravel.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateUserProfileActivity extends AppCompatActivity {
    String uNametxt,fNametxt,passtxt,emailtxt,phonextxt;
    CircleImageView profileImage;
    EditText uName, fName, pass, email;
    Button updateBtn;
    private Uri imageUri;
    private String myUri;
    private StorageReference profileRef;
    private StorageTask uploadTask;
    private ProgressDialog dialog;
    FirebaseAuth mAuth;
    String arrive = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_user_profile);

        uName = findViewById(R.id.updateProfile_userName);
        fName = findViewById(R.id.updateProfile_fullName);
        email = findViewById(R.id.updateProfile_email);
        pass = findViewById(R.id.updateProfile_password);
        profileImage = findViewById(R.id.updateProfile_imageView);
        updateBtn = findViewById(R.id.updateBtn);
        updateBtn = findViewById(R.id.updateBtn);

        profileRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        mAuth = FirebaseAuth.getInstance();

        arrive = getIntent().getStringExtra("editProfile");

        Intent i = getIntent();
        uNametxt = i.getStringExtra("uName");
        fNametxt = i.getStringExtra("fName");
        emailtxt = i.getStringExtra("email");
        passtxt = i.getStringExtra("password");
        phonextxt = i.getStringExtra("phone");

        displayInfo();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // checker = "clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(UpdateUserProfileActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                profileImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error:"+error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayInfo() {
        uName.setText(uNametxt);
        fName.setText(fNametxt);
        email.setText(emailtxt);
        pass.setText(passtxt);
    }

    private void saveUserInfo() {
        if (TextUtils.isEmpty(fName.getText().toString())){
            Toast.makeText(this, "First name is required", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(fName.getText().toString())){
            Toast.makeText(this, "Last name is required", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(email.getText().toString())){
            Toast.makeText(this, "Email Address is required", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(pass.getText().toString())){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
        }else {
            uploadImage();
        }
    }

    private void uploadImage() {
        dialog = new ProgressDialog(UpdateUserProfileActivity.this);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.lodaing_dialog_loading);

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (imageUri!=null){
            final  StorageReference fileRef = profileRef.child(uid+".jpg");

            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        myUri = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("fName",fName.getText().toString());
                        userMap.put("uName",uName.getText().toString());
                        userMap.put("email",email.getText().toString());
                        userMap.put("password",pass.getText().toString());
                        userMap.put("uid",uid);
                        userMap.put("phone","+91"+phonextxt);
                        userMap.put("imageUrl", myUri);

                        ref.child("+91"+phonextxt).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog.dismiss();
                                    Intent i = new Intent(UpdateUserProfileActivity.this, UserLoginActivity.class);
                                    //i.putExtra("arrive", "signUp");
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    finish();
                                    Toast.makeText(UpdateUserProfileActivity.this, "Profile Updated, please login to continue.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(UpdateUserProfileActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{
                        Toast.makeText(UpdateUserProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
        }else{
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }
}
