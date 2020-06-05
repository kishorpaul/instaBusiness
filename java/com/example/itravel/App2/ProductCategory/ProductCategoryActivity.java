package com.example.itravel.App2.ProductCategory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

import com.bumptech.glide.Glide;
import com.example.itravel.App2.Common.DashBoardActivity;
import com.example.itravel.App2.Models.Products;
import com.example.itravel.App2.Users.ProductDetailActivity;
import com.example.itravel.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductCategoryActivity extends AppCompatActivity {
    private String cat;
    private RecyclerView recyclerView;
    private ImageView close;
    private TextView catText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_category);

        cat = getIntent().getStringExtra("cat");
        catText = findViewById(R.id.cat_product_catName);
        recyclerView = findViewById(R.id.cat_product_recycler);
        close = findViewById(R.id.cat_product_close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductCategoryActivity.super.onBackPressed();
            }
        });

        catText.setText("Discover by "+cat);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(dbRef.orderByChild("pCategory").equalTo(cat), Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductCategoryViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductCategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductCategoryViewHolder productCategoryViewHolder, int i, @NonNull final Products products) {
                productCategoryViewHolder.pName.setText(products.getpName());
                productCategoryViewHolder.pPrice.setText(products.getpPrice()+"$");
                Glide.with(ProductCategoryActivity.this).load(products.getpImageUrl()).into(productCategoryViewHolder.image);

                productCategoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(ProductCategoryActivity.this, ProductDetailActivity.class);
                        i.putExtra("pid", products.getPid());
                        i.putExtra("pImageUrl", products.getpImageUrl());
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public ProductCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_category, parent, false);
               return new ProductCategoryViewHolder(v);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ProductCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image;
        TextView pName, pPrice;

        public ProductCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_product_cat_pImage);
            pName = itemView.findViewById(R.id.item_product_cat_pName);
            pPrice = itemView.findViewById(R.id.item_product_cat_pPrice);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
