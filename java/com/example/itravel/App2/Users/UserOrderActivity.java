package com.example.itravel.App2.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itravel.App2.Models.Orders;
import com.example.itravel.App2.Prevalent.Prevalent;
import com.example.itravel.App2.ViewHolder.OrderHistoryViewHolder;
import com.example.itravel.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class UserOrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView orders_emptyText;
    private ImageView back;
    String curPh;
    DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_order);

        recyclerView = findViewById(R.id.orders_recycler);
        orders_emptyText = findViewById(R.id.orders_emptyText);
        back = findViewById(R.id.user_orderBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserOrderActivity.super.onBackPressed();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(UserOrderActivity.this));
        recyclerView.setHasFixedSize(true);

        Paper.init(this);
        curPh = Paper.book().read(Prevalent.UserPhoneKey);
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("User View").child("+91"+curPh);

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    orders_emptyText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }else{
                    onStart();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        orders_emptyText.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        FirebaseRecyclerOptions<Orders> options = new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(orderRef, Orders.class).build();

        FirebaseRecyclerAdapter<Orders, OrderHistoryViewHolder> adapter = new FirebaseRecyclerAdapter<Orders, OrderHistoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderHistoryViewHolder orderHistoryViewHolder, int i, @NonNull Orders orders) {
                orderHistoryViewHolder.amount.setText(orders.getTotalAmount()+"$");
                orderHistoryViewHolder.date.setText(orders.getOrderDate()+" | "+orders.getOrderTime());
                orderHistoryViewHolder.through.setText("via - "+orders.getOrderThrough());
                orderHistoryViewHolder.state.setText(orders.getState());
                if (orders.getPaytmOrderId().equals("")){
                    orderHistoryViewHolder.orderId.setText(orders.getOrderId());
                }else if(orders.getOrderId().equals("")){
                    orderHistoryViewHolder.orderId.setText(orders.getPaytmOrderId());
                }
            }

            @NonNull
            @Override
            public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_history, parent, false);
                return new OrderHistoryViewHolder(v);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
     }
}
