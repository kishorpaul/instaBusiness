package com.example.itravel.App2.Sellers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itravel.App2.Models.Products;
import com.example.itravel.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddNewProductActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ArrayList<Products> data;
    private String category="", valName,valDesc,valPrice,curDate,curTime, productRandomKey;
    private ImageView backward, addNewImage;
    private TextView toolBarText;
    private EditText prodName,prodDesc,prodPrice;
    private static final int GALLERY_PICK = 1;
    private Uri imageUri;
    private Button addProductBtn;
    private StorageReference prodRef;
    private String downloadUrl;
    private DatabaseReference productRef, sellerRef, notifyRef;
    private ProgressDialog loadingdialog;
    private FirebaseAuth mAuth;
    private String sellerName, sellerId, sellerPhone, sellerEmail,sellerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_seller_add_new_product);

        backward = findViewById(R.id.addProductBack);
        toolBarText = findViewById(R.id.addName);
        addNewImage = findViewById(R.id.addProductImage);
        prodName = findViewById(R.id.addProductName);
        prodDesc = findViewById(R.id.addProductDesc);
        prodPrice = findViewById(R.id.addProductPrice);
        addProductBtn  =findViewById(R.id.addProductBtn);

        mAuth = FirebaseAuth.getInstance();

        prodRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        sellerRef = FirebaseDatabase.getInstance().getReference().child("Sellers");
        notifyRef = FirebaseDatabase.getInstance().getReference().child("Notifications");

        category = getIntent().getStringExtra("category").toString();
        Toast.makeText(this, "Cat:"+category, Toast.LENGTH_SHORT).show();
        toolBarText.setText("Add a "+category+" Product");

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellerAddNewProductActivity.this, SellerHomeActivity .class);
                startActivity(i);
                finish();
            }
        });

        addNewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
                //  mDialog.show();
            }
        });

        sellerRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    sellerId = dataSnapshot.child("uid").getValue().toString();
                    sellerName = dataSnapshot.child("name").getValue().toString();
                    sellerPhone = dataSnapshot.child("phone").getValue().toString();
                    sellerEmail = dataSnapshot.child("email").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void OpenGallery() {
        Intent galleryInt = new Intent();
        galleryInt.setAction(Intent.ACTION_GET_CONTENT);
        galleryInt.setType("image/*");
        startActivityForResult(galleryInt, GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_PICK && resultCode ==RESULT_OK && data!=null){
            imageUri = data.getData();
            addNewImage.setImageURI(imageUri);
        }
    }

    private void validateProductData() {
        valName = prodName.getText().toString();
        valDesc = prodDesc.getText().toString();
        valPrice = prodPrice.getText().toString();

        if (imageUri ==null){
            Toast.makeText(this, "Product image is required", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(valName)){
            Toast.makeText(this, "Product name is required", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(valDesc)){
            Toast.makeText(this, "Product description is required", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(valPrice)){
            Toast.makeText(this, "Product price is required", Toast.LENGTH_SHORT).show();
        }else{
            StoreProductInfo();
        }
    }

    private void StoreProductInfo() {

        loadingdialog = new ProgressDialog(this);
        loadingdialog.show();
        loadingdialog.setContentView(R.layout.loading_please_wait);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //String curDate, curTime;
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat date =new SimpleDateFormat("MMM dd, yyyy");
        curDate = date.format(cal.getTime());

        SimpleDateFormat time =new SimpleDateFormat("HH:mm:ss a");
        curTime = time.format(cal.getTime());

        productRandomKey = curDate+curTime;

        final StorageReference filePath = prodRef.child(imageUri.getLastPathSegment() + productRandomKey+".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SellerAddNewProductActivity.this, "Error:"+e.toString(), Toast.LENGTH_SHORT).show();
                loadingdialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SellerAddNewProductActivity.this, "Image Uploaded to Firestore", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadUrl = filePath.getDownloadUrl().toString();
                        return  filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadUrl = task.getResult().toString();
                            Log.d("Url",downloadUrl);
                            Toast.makeText(SellerAddNewProductActivity.this, "Getting product Image Url", Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDB();
                            // mDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDB() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("pDate", curDate);
        productMap.put("pTime", curTime);
        productMap.put("pName", valName);
        productMap.put("pDesc", valDesc);
        productMap.put("pPrice", valPrice);
        productMap.put("pCategory", category);
        productMap.put("pImageUrl", downloadUrl);
        productMap.put("verifyStat", "unverified");
        productMap.put("sellerEmail", sellerEmail);
        productMap.put("sellerPhone", sellerPhone);
        productMap.put("sellerName", sellerName);
        productMap.put("sellerId", sellerId);

        productRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            HashMap<String, String> notifyMap= new HashMap<>();
                            notifyMap.put("from", sellerId);
                            notifyMap.put("pid", productRandomKey);

                            notifyRef.child(sellerId).push().setValue(notifyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent i =new Intent(SellerAddNewProductActivity.this, SellerHomeActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                        loadingdialog.dismiss();
                                        Toast.makeText(SellerAddNewProductActivity.this, "Product is live", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(SellerAddNewProductActivity.this, "Error :"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        loadingdialog.dismiss();
                                    }
                                }
                            });
                        }else{
                            loadingdialog.dismiss();
                            Toast.makeText(SellerAddNewProductActivity.this, "Error"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
