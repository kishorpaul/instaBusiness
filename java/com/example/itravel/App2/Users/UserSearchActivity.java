package com.example.itravel.App2.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.itravel.App2.Models.Products;
import com.example.itravel.App2.ViewHolder.UserSearchViewHolder;
import com.example.itravel.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserSearchActivity extends AppCompatActivity {
    ImageView searchBtn, close;
    EditText searchInput;
    RecyclerView recyclerView;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_search);

        searchBtn = findViewById(R.id.user_search_image);
        searchInput = findViewById(R.id.user_search_input);
        recyclerView = findViewById(R.id.user_search_recycler);
        close = findViewById(R.id.user_search_close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSearchActivity.super.onBackPressed();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setContentView(R.layout.loading_loading);
        dialog.setCanceledOnTouchOutside(false);

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(dbRef
                .orderByChild("pName").startAt(searchInput.getText().toString()), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, UserSearchViewHolder> adapter = new FirebaseRecyclerAdapter<Products, UserSearchViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserSearchViewHolder userSearchViewHolder, int i, @NonNull final Products products) {
                Glide.with(UserSearchActivity.this).load(products.getpImageUrl()).into(userSearchViewHolder.image);
                userSearchViewHolder.name.setText(products.getpName());
                userSearchViewHolder.price.setText(products.getpPrice()+"$");
                userSearchViewHolder.desc.setText(products.getpDesc());

                userSearchViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pid = products.getPid();
                        Intent i = new Intent(UserSearchActivity.this, ProductDetailActivity.class);
                        i.putExtra("pid", pid);
                        i.putExtra("pImageUrl", products.getpImageUrl());
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public UserSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
               return new UserSearchViewHolder(v);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        dialog.dismiss();
    }
}
