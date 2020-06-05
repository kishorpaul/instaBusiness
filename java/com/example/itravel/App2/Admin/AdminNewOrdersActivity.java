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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itravel.App2.Models.Orders;
import com.example.itravel.App2.Prevalent.Prevalent;
import com.example.itravel.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class AdminNewOrdersActivity extends AppCompatActivity {
    private ImageView back;
    private RecyclerView recyclerView;
    private DatabaseReference orderRef;
    String curPh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_new_orders);

        back = findViewById(R.id.allOrder_Back);
        recyclerView = findViewById(R.id.all_order_recycler);
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("Admin View");

        Paper.init(this);
        curPh = Paper.book().read(Prevalent.UserPhoneKey);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AdminNewOrdersActivity.super.onBackPressed();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Orders> options = new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(orderRef, Orders.class).build();

        FirebaseRecyclerAdapter<Orders, OrderViewHolder> adapter = new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, final int i, @NonNull final Orders orders) {
                orderViewHolder.txtuserName.setText("Name: "+orders.getUserName());
                orderViewHolder.txtphone.setText("Ph: "+orders.getPhone());
                orderViewHolder.txtttlPrice.setText("Total Price: "+orders.getTotalAmount()+"$");
                orderViewHolder.txtaddress.setText("Address: "+orders.getAddress());
                orderViewHolder.txtdateTime.setText("Date: "+orders.getOrderDate()+" | "+orders.getOrderTime());

                orderViewHolder.viewAllBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String oid = getRef(i).getKey();
                        Intent i = new Intent(AdminNewOrdersActivity.this, AdminViewAllProductsActivity.class);
                        i.putExtra("oId", oid);
                        startActivity(i);
                    }
                });

                orderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence[] options = new CharSequence[]{
                                "Yes",
                                "No"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        builder.setTitle("Products Shipped?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0){
                                    String oid = getRef(i).getKey();
                                    removeOrder(oid, orders.getUserName(),orders.getPhone(),orders.getTotalAmount());
                                }else if (which==1){
                                    dialog.dismiss();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orders,parent,false);
                return  new OrderViewHolder(v);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeOrder(final String oid, final String userName, final String phone, final String amt) {
        orderRef.child(oid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                final DatabaseReference fullfilledRef = FirebaseDatabase.getInstance().getReference().child("Fullfilled Orders")
                        .child("Admin View");

                Calendar cal = Calendar.getInstance();
                String curTime, curDate;
                SimpleDateFormat date = new SimpleDateFormat("MMM dd,yyyy");
                curDate = date.format(cal.getTime());

                SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
                curTime = time.format(cal.getTime());

                String pp = "+91"+curPh;

                final HashMap<String, Object> fullfilledMap = new HashMap<>();
                fullfilledMap.put("shippedDate",curDate);
                fullfilledMap.put("shippedTime",curTime);
                fullfilledMap.put("shippedId",oid);
                fullfilledMap.put("shippedForOwner", pp);
                fullfilledMap.put("shippedState","Shipped.");
                fullfilledMap.put("shippedUserName",userName);
                fullfilledMap.put("shippedPhone",phone);
                fullfilledMap.put("shippedTotalPrice",amt);

                fullfilledRef.push().setValue(fullfilledMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

                        orderRef.child("User View").child(oid).child(oid.substring(3,13)).child("state").setValue("Shipped");

                        Toast.makeText(AdminNewOrdersActivity.this, "Item added to history", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder{
        public TextView txtuserName, txtphone, txtttlPrice, txtdateTime,txtaddress;
        public Button viewAllBtn;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtuserName = itemView.findViewById(R.id.item_order_uName);
            txtphone = itemView.findViewById(R.id.item_order_contact);
            txtaddress = itemView.findViewById(R.id.item_order_address);
            txtttlPrice = itemView.findViewById(R.id.item_order_ttlPrice);
            txtdateTime = itemView.findViewById(R.id.item_order_date);
            viewAllBtn = itemView.findViewById(R.id.item_order_ViewAll);
        }
    }
}
