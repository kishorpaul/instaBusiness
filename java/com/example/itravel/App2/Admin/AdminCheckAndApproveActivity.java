package com.example.itravel.App2.Admin;

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

public class AdminCheckAndApproveActivity extends AppCompatActivity {
    private TextView back;
    private RecyclerView recyclerView;
    private DatabaseReference unVerified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_check_and_approve);

        recyclerView = findViewById(R.id.admin_CAA_recycler);
        back = findViewById(R.id.admin_CAA_Back);

        unVerified = FirebaseDatabase.getInstance().getReference().child("Products");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminCheckAndApproveActivity.this, AdminHomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unVerified.orderByChild("verifyStat").equalTo("unverified"), Products.class).build();

        FirebaseRecyclerAdapter<Products, SellerProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, SellerProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerProductViewHolder productViewHolder, int i, @NonNull final Products product) {
                productViewHolder.prodPrice.setText("Price :"+ product.getpPrice()+"$");
                productViewHolder.prodDesc.setText(product.getpDesc());
                productViewHolder.prodName.setText(product.getpName());
                productViewHolder.pStat.setText(product.getVerifyStat());
                Glide.with(AdminCheckAndApproveActivity.this).load(product.getpImageUrl()).into(productViewHolder.prodImage);

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String pid = product.getPid();
                        CharSequence[] options = new CharSequence[]{
                                "Yes",
                                "No"
                        };

                        final AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckAndApproveActivity.this);
                        builder.setTitle("Do you want to Approve this product?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                View v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seller_product, parent, false);
                return  new SellerProductViewHolder(v);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void ChangeProdState(String pid) {
        unVerified.child(pid).child("verifyStat").setValue("approved").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(AdminCheckAndApproveActivity.this, AdminHomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    Toast.makeText(AdminCheckAndApproveActivity.this, "Product Approved and is Live", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
