package com.example.itravel.App2.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.itravel.App2.Common.DashBoardActivity;
import com.example.itravel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerHomeActivity extends AppCompatActivity {
    private CardView addProd, allProd,logout;
    private ProgressDialog loadingdialog;
    private ImageView mobiles,laptops,headphones,watches;
    private ImageView glasses,bags,hats,shoes;
    TextView userName;
    String fUser,fid;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_seller_home);

        mAuth = FirebaseAuth.getInstance();
        fid = mAuth.getCurrentUser().getUid();

        allProd = findViewById(R.id.seller_all_products);
        logout = findViewById(R.id.seller_logout);

        mobiles = findViewById(R.id.mobiles);
        laptops = findViewById(R.id.laptops);
        headphones = findViewById(R.id.headphones);
        watches = findViewById(R.id.watches);
        glasses = findViewById(R.id.sunglasses);
        bags = findViewById(R.id.bags);
        hats = findViewById(R.id.hat);
        shoes = findViewById(R.id.shoes);
        userName = findViewById(R.id.seller_home_userName);

        userName.setText(fUser);

        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellerHomeActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("category","Mobile");
                startActivity(i);
            }
        });

        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellerHomeActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("category","Laptop");
                startActivity(i);
            }
        });

        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellerHomeActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("category","Headphone");
                startActivity(i);
            }
        });

        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellerHomeActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("category","Watch");
                startActivity(i);
            }
        });

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellerHomeActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("category","Glasses");
                startActivity(i);
            }
        });

        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellerHomeActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("category","Hat");
                startActivity(i);
            }
        });

        bags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellerHomeActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("category","Bag");
                startActivity(i);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellerHomeActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("category","Shoe");
                startActivity(i);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingdialog = new ProgressDialog(SellerHomeActivity.this);
                loadingdialog.show();
                loadingdialog.setContentView(R.layout.loading_please_wait);
                loadingdialog.setCanceledOnTouchOutside(false);
                loadingdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                mAuth.signOut();
                Intent i = new Intent(SellerHomeActivity.this, DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                loadingdialog.dismiss();
            }
        });

        allProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(SellerHomeActivity.this, SellerAllProductsActivity.class);
               i.putExtra("fid", fid);
               startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        dbRef.child(fid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    userName.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SellerHomeActivity.this, DashBoardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
