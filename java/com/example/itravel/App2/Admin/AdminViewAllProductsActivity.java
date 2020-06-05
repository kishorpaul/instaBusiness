package com.example.itravel.App2.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.itravel.App2.Models.Cart;
import com.example.itravel.App2.ViewHolder.CartViewHolder;
import com.example.itravel.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminViewAllProductsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView orderId;
    private ImageView back;
    private DatabaseReference cartListRef;
    private String oId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_view_all_products);

        recyclerView = findViewById(R.id.view_all_order_recycler);
        orderId = findViewById(R.id.viewAllOrder_Name);
        back = findViewById(R.id.viewallOrder_Back);

        oId = getIntent().getStringExtra("oId");

        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View")
                .child(oId).child("Products");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AdminViewAllProductsActivity.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef, Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {
                cartViewHolder.pName.setText(cart.getpName());
                cartViewHolder.pPrice.setText("Price: "+cart.getpPrice()+"$");
                cartViewHolder.pQuantity.setText("Quantity: "+cart.getQuantity());
                orderId.setText(cart.getPid());
                Glide.with(AdminViewAllProductsActivity.this).load(cart.getpImageUrl()).placeholder(R.drawable.cash02)
                        .into(cartViewHolder.image);
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
                return new CartViewHolder(v);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
