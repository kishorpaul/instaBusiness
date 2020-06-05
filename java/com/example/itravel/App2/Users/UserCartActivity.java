package com.example.itravel.App2.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.itravel.App2.Models.Cart;
import com.example.itravel.App2.Prevalent.Prevalent;
import com.example.itravel.App2.ViewHolder.CartViewHolder;
import com.example.itravel.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

import static com.paytm.pgsdk.easypay.manager.PaytmAssist.getContext;

public class UserCartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button continueBtn;
    private TextView noItemText,orderSubmittedText;
    ImageView closeCart;
    private int ttlPrice=0;
    String curPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_cart);

        recyclerView = findViewById(R.id.cart_recycler);
        continueBtn = findViewById(R.id.cart_continueBtn);
        noItemText = findViewById(R.id.cart_NoItemText);
        orderSubmittedText = findViewById(R.id.cart_OrderSubmittedText);
        closeCart = findViewById(R.id.wishList_close);

        closeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserCartActivity.super.onBackPressed();
            }
        });

        Paper.init(this);
        curPhone = Paper.book().read(Prevalent.UserPhoneKey);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(UserCartActivity.this, UserConfirmActivity.class);
                i.putExtra("price",String.valueOf(ttlPrice));
                i.putExtra("curPhone", curPhone);
                startActivity(i);
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderState();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        cartListRef.child("User View").child("+91"+curPhone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    noItemText.setVisibility(View.INVISIBLE);
                    continueBtn.setVisibility(View.VISIBLE);
                } else if (!dataSnapshot.exists()) {
                    noItemText.setVisibility(View.VISIBLE);
                    continueBtn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (curPhone != null) {

            FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                    .setQuery(cartListRef.child("User View")
                            .child("+91"+curPhone).child("Products"), Cart.class).build();

            FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {
                    cartViewHolder.pName.setText(cart.getpName());
                    cartViewHolder.pPrice.setText("Price: " + cart.getpPrice() + "$");
                    cartViewHolder.pQuantity.setText("Quantity: " + cart.getQuantity());
                    Glide.with(UserCartActivity.this).load(cart.getpImageUrl()).placeholder(R.drawable.cart).into(cartViewHolder.image);

                    int oneTypeTPrice = ((Integer.valueOf(cart.getpPrice()))) * Integer.valueOf(cart.getQuantity());
                    ttlPrice = ttlPrice + oneTypeTPrice;

                    cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
                            final CharSequence options[] = new CharSequence[]{
                                    "Edit",
                                    "Remove"
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(UserCartActivity.this);
                            builder.setTitle("WishList Options");

                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        Intent i = new Intent(UserCartActivity.this, ProductDetailActivity.class);
                                        i.putExtra("pid", cart.getPid());
                                        startActivity(i);
                                    }
                                    if (which == 1) {
                                        cartListRef.child("User View").child("+91"+curPhone).child("Products")
                                                .child(cart.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(UserCartActivity.this, "WishList Removed from User", Toast.LENGTH_SHORT).show();
                                                    cartListRef.child("Admin View").child("+91"+curPhone).child("Products")
                                                            .child(cart.getPid()).removeValue();
                                                    // startActivity(new Intent(getContext(), CartFragment.class));
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                            builder.show();
                        }
                    });
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
        }else{
            Toast.makeText(this, "Please log in to see your item", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkOrderState() {
        if (curPhone != null) {
            DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("+91" + curPhone).child(curPhone);

            orderRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String shipmentState = dataSnapshot.child("state").getValue().toString();
                        String userName = dataSnapshot.child("userName").getValue().toString();

                        if (shipmentState.equals("shipped")) {
                            noItemText.setVisibility(View.INVISIBLE);
                            continueBtn.setVisibility(View.INVISIBLE);
                            orderSubmittedText.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            orderSubmittedText.setText("Dear " + userName + ", \nYour order is in shipping process, expect the delivery soon \nThank you.");
                        } else if (shipmentState.equals("not shipped")) {
                            noItemText.setVisibility(View.INVISIBLE);
                            continueBtn.setVisibility(View.INVISIBLE);
                            orderSubmittedText.setVisibility(View.VISIBLE);
                            orderSubmittedText.setText("Dear " + userName + ", \nYour submission on the order is in progress. Please wait as you will hear from us soon.\nThank you");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
