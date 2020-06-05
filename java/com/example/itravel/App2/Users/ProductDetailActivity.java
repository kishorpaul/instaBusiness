package com.example.itravel.App2.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.itravel.App2.Common.DashBoardActivity;
import com.example.itravel.App2.Models.Products;
import com.example.itravel.App2.Prevalent.Prevalent;
import com.example.itravel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView productImage,back;
    private TextView pName,pDesc,pPrice;
    private ElegantNumberButton numberBtn;
    private Button addToCartBtn;
    private String pid = "",pImageUrl= "";
    private String curTime, curDate,state="normal";
    private ProgressBar progressBar;
    String currOnlinePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_detail);
        productImage = findViewById(R.id.productDetail_image);

        pName = findViewById(R.id.productDetail_pName);
        pDesc = findViewById(R.id.productDetail_pDesc);
        pPrice = findViewById(R.id.productDetail_pPrice);
        numberBtn = findViewById(R.id.productDetail_pNo);
        back = findViewById(R.id.productDetail_back);
        addToCartBtn = findViewById(R.id.productDetail_placeOrderBtn);
        progressBar = findViewById(R.id.product_detail_progress);

        pid = getIntent().getStringExtra("pid");
        pImageUrl = getIntent().getStringExtra("pImageUrl");

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state.equals("Order Placed") || state.equals("Order Shipped")){
                    Toast.makeText(ProductDetailActivity.this, "You cannot add another item to cart while the current order is in process", Toast.LENGTH_LONG).show();
                }else{
                    addingToCartList();
                }
            }
        });

        Paper.init(this);
        currOnlinePhone =  Paper.book().read(Prevalent.UserPhoneKey);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetailActivity.super.onBackPressed();
            }
        });

        getProductDetails(pid);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //checkOrderState();
    }

    private void addingToCartList() {
        if (currOnlinePhone==null){
            progressBar.setVisibility(View.VISIBLE);
            addToCartBtn.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "You have not logged in to add to cart", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            addToCartBtn.setVisibility(View.VISIBLE);
        }
        else if (currOnlinePhone!=null){
            progressBar.setVisibility(View.VISIBLE);
            addToCartBtn.setVisibility(View.INVISIBLE);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat date = new SimpleDateFormat("MMM dd, yyyy");
            curDate = date.format(calendar.getTime());

            SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
            curTime = time.format(calendar.getTime());

            final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
            final HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("pid", pid);
            cartMap.put("pImageUrl", pImageUrl);
            cartMap.put("pName", pName.getText().toString());
            cartMap.put("pDesc", pDesc.getText().toString());
            cartMap.put("pPrice", pPrice.getText().toString());
            cartMap.put("pDate", curDate);
            cartMap.put("pTime", curTime);
            cartMap.put("quantity", numberBtn.getNumber());
            cartMap.put("discount", "");
            //cartMap.put("pImageUrl",pImageUrl);

            cartRef.child("User View").child("+91" + currOnlinePhone).child("Products").child(pid)
                    .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        cartRef.child("Admin View").child("+91" + currOnlinePhone).child("Products").child(pid)
                                .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ProductDetailActivity.this, "Product added to cart", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(ProductDetailActivity.this, DashBoardActivity.class);
                                    startActivity(i);
                                    finish();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    addToCartBtn.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        addToCartBtn.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    private void getProductDetails(String pid) {
        final DatabaseReference prodRef = FirebaseDatabase.getInstance().getReference().child("Products");
        prodRef.child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    try {
                        Products product = dataSnapshot.getValue(Products.class);
                        assert product != null;
                        pName.setText(product.getpName());
                        pDesc.setText(product.getpDesc());
                        pPrice.setText(product.getpPrice());
                        Glide.with(ProductDetailActivity.this).load(product.getpImageUrl()).placeholder(R.drawable.android)
                                .into(productImage);
                    }catch (Exception e){
                        Toast.makeText(ProductDetailActivity.this, "Error:"+e, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    private void checkOrderState(){
//        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
//
//        orderRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()){
//                    String shipmentState = dataSnapshot.child("state").getValue().toString();
//                    String userName = dataSnapshot.child("userName").getValue().toString();
//
//                    if (shipmentState.equals("shipped")){
//                        state = "Order Shipped";
//                    }else if(shipmentState.equals("not shipped")){
//                        state = "Order Placed";
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}

