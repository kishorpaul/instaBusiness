package com.example.itravel.App2.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.itravel.App2.Models.Products;
import com.example.itravel.App2.ViewHolder.SellerProductViewHolder;
import com.example.itravel.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellerAllProductsActivity extends AppCompatActivity {
    private TextView back;
    private RecyclerView recyclerView;
    private DatabaseReference pRef;
    String fid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_seller_all_products);

        back = findViewById(R.id.seller_all_prod_back);
        recyclerView = findViewById(R.id.seller_all_prod_recycler);

        pRef = FirebaseDatabase.getInstance().getReference().child("Products");
        fid = getIntent().getStringExtra("fid");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellerAllProductsActivity.this, SellerHomeActivity .class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(pRef.orderByChild("sellerId").equalTo(fid), Products.class).build();

        FirebaseRecyclerAdapter<Products, SellerProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, SellerProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerProductViewHolder sellerProductViewHolder, int i, @NonNull final Products product) {
                sellerProductViewHolder.prodDesc.setText(product.getpDesc());
                sellerProductViewHolder.prodPrice.setText("Price :"+product.getpPrice()+"$");
                sellerProductViewHolder.prodName.setText(product.getpName().toUpperCase());
                sellerProductViewHolder.pStat.setText(product.getVerifyStat());
                Glide.with(SellerAllProductsActivity.this).load(product.getpImageUrl()).into(sellerProductViewHolder.prodImage);

                sellerProductViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence[] options = new CharSequence[]{
                                "Yes",
                                "No"
                        };

                        final AlertDialog.Builder builder = new AlertDialog.Builder(SellerAllProductsActivity.this);
                        builder.setTitle("Do you want to Delete this product?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String pid = product.getPid();
                                if (which == 0){
                                    ChangeProdState(pid);
                                }else if(which == 1){
                                    builder.setCancelable(true);
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public SellerProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seller_product, parent, false);
                return  new SellerProductViewHolder(v);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void ChangeProdState(String pid) {
        pRef.child(pid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent i = new Intent(SellerAllProductsActivity.this, SellerHomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    Toast.makeText(SellerAllProductsActivity.this, "Product Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
