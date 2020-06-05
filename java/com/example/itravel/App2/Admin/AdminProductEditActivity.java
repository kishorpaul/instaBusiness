package com.example.itravel.App2.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.itravel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminProductEditActivity extends AppCompatActivity {
    private EditText pPrice,pName,pDesc;
    private Button applyBtn,deleteBtn;
    private ImageView back,pImage;
    private String pId="";
    private DatabaseReference pRef;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_edit);

        applyBtn = findViewById(R.id.admin_editProduct_ApplyBtn);
        pPrice = findViewById(R.id.admin_editProduct_price);
        pName = findViewById(R.id.admin_editProduct_name);
        pDesc = findViewById(R.id.admin_editProduct_desc);
        back = findViewById(R.id.admin_editProduct_Back);
        pImage = findViewById(R.id.admin_editProduct_Image);
        progressBar = findViewById(R.id.admin_editProduct_progressBar);
        deleteBtn = findViewById(R.id.admin_editProduct_deleteBtn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedProduct();
            }
        });

        pId = getIntent().getStringExtra("pid");

        pRef = FirebaseDatabase.getInstance().getReference().child("Products").child(pId);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminProductEditActivity.this, AdminManageProductsActivity.class));
                getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
            }
        });

        displaySpecificProdInfo();

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });
    }

    private void deleteSelectedProduct() {
        progressBar.setVisibility(View.VISIBLE);
        applyBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setVisibility(View.INVISIBLE);

        pRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminProductEditActivity.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminProductEditActivity.this, AdminManageProductsActivity.class));
                finish();
                progressBar.setVisibility(View.INVISIBLE);
                applyBtn.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void applyChanges() {
        String txtname = pName.getText().toString();
        String txtprice = pPrice.getText().toString();
        String txtdesc = pDesc.getText().toString();

        if (TextUtils.isEmpty(txtname)){
            Toast.makeText(this, "Product Name is required", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(txtprice)){
            Toast.makeText(this, "Product Price is required", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(txtdesc)){
            Toast.makeText(this, "Product Description is required", Toast.LENGTH_SHORT).show();
        }else{
            saveInfo(txtname,txtprice,txtdesc);
        }
    }

    private void saveInfo(String txtname, String txtprice, String txtdesc) {
        progressBar.setVisibility(View.VISIBLE);
        applyBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setVisibility(View.INVISIBLE);
        HashMap<String,Object> pMap = new HashMap<>();
        pMap.put("pid",pId);
        pMap.put("pName",txtname);
        pMap.put("pPrice",txtprice);
        pMap.put("pDesc",txtdesc);

        pRef.updateChildren(pMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);
                    applyBtn.setVisibility(View.VISIBLE);
                    deleteBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(AdminProductEditActivity.this, "Product changes are applied", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AdminProductEditActivity.this, AdminManageProductsActivity.class));
                    finish();
                }
            }
        });
    }


    private void displaySpecificProdInfo() {
        pRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String name = dataSnapshot.child("pName").getValue().toString();
                    String price = dataSnapshot.child("pPrice").getValue().toString();
                    String desc = dataSnapshot.child("pDesc").getValue().toString();
                    String imageUrl = dataSnapshot.child("pImageUrl").getValue().toString();

                    pName.setText(name);
                    pPrice.setText(price);
                    pDesc.setText(desc);
                    try {
                        Glide.with(AdminProductEditActivity.this).load(imageUrl).placeholder(R.drawable.google).into(pImage);
                    }catch (Exception e){
                        Toast.makeText(AdminProductEditActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
