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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.itravel.App2.Models.Products;
import com.example.itravel.App2.ViewHolder.ManageProductViewHolder;
import com.example.itravel.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminManageProductsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView back;
    private DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_manage_products);

        dbRef = FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView = findViewById(R.id.admin_manage_recycler);
        back = findViewById(R.id.admin_manage_Back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminManageProductsActivity.this, AdminHomeActivity.class));
                getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

       FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
               .setQuery(dbRef.orderByChild("verifyStat").equalTo("approved"), Products.class).build();

       FirebaseRecyclerAdapter<Products, ManageProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ManageProductViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull ManageProductViewHolder manageProductViewHolder, int i, @NonNull final Products products) {
               manageProductViewHolder.pName.setText(products.getpName());
               manageProductViewHolder.pDesc.setText(products.getpDesc());
               manageProductViewHolder.pPrice.setText(products.getpPrice());
               Glide.with(AdminManageProductsActivity.this).load(products.getpImageUrl())
                       .placeholder(R.drawable.google).into(manageProductViewHolder.pImage);

               manageProductViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                      Intent i = new Intent(AdminManageProductsActivity.this, AdminProductEditActivity.class);
                      i.putExtra("pid", products.getPid());
                      startActivity(i);
                   }
               });
           }

           @NonNull
           @Override
           public ManageProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_product_, parent,false);
               return new ManageProductViewHolder(v);
           }
       };
       recyclerView.setAdapter(adapter);
       adapter.startListening();
    }
}
