package com.example.itravel.App2.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.itravel.App2.Common.DashBoardActivity;
import com.example.itravel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserConfirmActivity extends AppCompatActivity {
    private Button confOrderBtn;
    private EditText name, phone,address,city;
    private TextView totalPrice;
    private ImageView back,cashOnDelivery,paytmTxt;
    private String ttlPrice;
    private ProgressDialog dialog, loadingdialog;
    private RelativeLayout orderConfirmationLayout;
    private Button continueDisc;
    private TextView orderIdTxtView,orderDateTxtView;
    String curPhone, orderId;
    String ouid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_confirm);

        confOrderBtn = findViewById(R.id.confirm_finalOrderBtn);
        name = findViewById(R.id.confirm_userName);
        phone = findViewById(R.id.confirm_userPhone);
        address = findViewById(R.id.confirm_userAddress);
        city = findViewById(R.id.confirm_userCity);
        totalPrice = findViewById(R.id.confirm_totalPrice);
        back = findViewById(R.id.confirm_back);
        orderConfirmationLayout = findViewById(R.id.order_conformation_layout);
        orderDateTxtView = findViewById(R.id.confirm_orderDate);

        orderIdTxtView = findViewById(R.id.confirm_orderId);
        continueDisc = findViewById(R.id.confirm_continueDiscovery);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserConfirmActivity.super.onBackPressed();
            }
        });

        confOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });

        ttlPrice = getIntent().getStringExtra("price");
        curPhone = getIntent().getStringExtra("curPhone");
        totalPrice.setText("Total Price: "+ttlPrice);
    }

    private void checkFields() {
        if (TextUtils.isEmpty(name.getText().toString())){
            Toast.makeText(this, "Please provide your name", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phone.getText().toString())){
            Toast.makeText(this, "Please provide your phone number", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(address.getText().toString())){
            Toast.makeText(this, "Please provide your Residence address", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(city.getText().toString())){
            Toast.makeText(this, "Please provide your city", Toast.LENGTH_SHORT).show();
        }else{
            proceedWithPayment();
        }
    }

    private void proceedWithPayment() {
        loadingdialog = new ProgressDialog(this);
        loadingdialog.show();
        loadingdialog.setContentView(R.layout.payment_method_dialog);
        loadingdialog.setCanceledOnTouchOutside(true);
        loadingdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        paytmTxt = loadingdialog.findViewById(R.id.payment_paytm);
        cashOnDelivery = loadingdialog.findViewById(R.id.payment_cahOnDelivery);

        cashOnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmOrder();
            }
        });

        paytmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(UserConfirmActivity.this);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.lodaing_dialog_loading);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                // paymentDialog.dismiss();
                loadingdialog.dismiss();

                if (ContextCompat.checkSelfPermission(UserConfirmActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(UserConfirmActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                }

                final String mId = "qFFivz80682499914053";
                final String customerId = curPhone;
                final String orderId = UUID.randomUUID().toString().substring(0, 28);
                String url = "https://kishorpaul.000webhostapp.com/paytm/generateChecksum.php";
                final String callBackUrl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

                RequestQueue requestQueue = Volley.newRequestQueue(UserConfirmActivity.this);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("CHECKSUMHASH")){
                                String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");

                                PaytmPGService paytmPGService = PaytmPGService.getStagingService();
                                HashMap<String, String> paraMap = new HashMap<String, String>();
                                paraMap.put("MID", mId);
                                paraMap.put("ORDER_ID", orderId);
                                paraMap.put("CUST_ID", customerId);
                                paraMap.put("CHANNEL_ID", "WAP");
                                paraMap.put("TXN_AMOUNT", ttlPrice);
                                paraMap.put("WEBSITE","WEBSTAGING");
                                paraMap.put("INDUSTRY_TYPE_ID", "Retail");
                                paraMap.put("CALLBACK_URL",callBackUrl);
                                paraMap.put("CHECKSUMHASH", CHECKSUMHASH);

                                PaytmOrder paytmOrder = new PaytmOrder(paraMap);

                                paytmPGService.initialize(paytmOrder, null);

                                paytmPGService.startPaymentTransaction(UserConfirmActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                                    @Override
                                    public void onTransactionResponse(final Bundle inResponse) {
                                        // Toast.makeText(ConfirmActivity.this, "Error"+inResponse.toString(), Toast.LENGTH_SHORT).show();
                                        if (inResponse.getString("STATUS").equals("TXN_SUCCESS")){
                                            final String pOid = inResponse.getString("ORDERID");
                                            final String pODate = inResponse.getString("TXNDATE");
                                            orderIdTxtView.setText("Order ID : "+pOid);
                                            orderDateTxtView.setText("Ordered on : "+pODate);
                                            orderConfirmationLayout.setVisibility(View.VISIBLE);
                                            dialog.dismiss();
                                            continueDisc.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog = new ProgressDialog(UserConfirmActivity.this);
                                                    dialog.show();
                                                    dialog.setCanceledOnTouchOutside(false);
                                                    dialog.setContentView(R.layout.lodaing_dialog_loading);
                                                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                    //paymentDialog.dismiss();
                                                    loadingdialog.dismiss();
                                                    final String curTime, curDate;
                                                    Calendar cal = Calendar.getInstance();

                                                    SimpleDateFormat date = new SimpleDateFormat("MMM dd,yyyy");
                                                    curDate = date.format(cal.getTime());

                                                    SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
                                                    curTime = time.format(cal.getTime());

                                                    ouid = curDate+curTime;

                                                    final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("User View").child("+91"+curPhone).child(curPhone);

                                                    HashMap<String, Object> orderMap = new HashMap<>();
                                                    orderMap.put("orderTime", curTime);
                                                    orderMap.put("orderDate", curDate);
                                                    orderMap.put("state", "In Progress");
                                                    orderMap.put("totalAmount", ttlPrice);
                                                    orderMap.put("userName", name.getText().toString());
                                                    orderMap.put("phone", phone.getText().toString());
                                                    orderMap.put("address", address.getText().toString());
                                                    orderMap.put("city", city.getText().toString());
                                                    orderMap.put("paytmOrderId", pOid);
                                                    orderMap.put("paytmOrderDate", pODate);
                                                    orderMap.put("orderId", "");
                                                    orderMap.put("orderStatus", "Not yet Delivered");
                                                    orderMap.put("orderThrough", "paytm");

                                                    orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
                                                                        .child("+91"+curPhone).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()){
                                                                            final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("Admin View").child("+91"+curPhone);

                                                                            HashMap<String, Object> orderMap = new HashMap<>();
                                                                            orderMap.put("orderTime", curTime);
                                                                            orderMap.put("orderDate", curDate);
                                                                            orderMap.put("state", "In Progress");
                                                                            orderMap.put("totalAmount", ttlPrice);
                                                                            orderMap.put("userName", name.getText().toString());
                                                                            orderMap.put("phone", phone.getText().toString());
                                                                            orderMap.put("address", address.getText().toString());
                                                                            orderMap.put("city", city.getText().toString());
                                                                            orderMap.put("paytmOrderId", pOid);
                                                                            orderMap.put("paytmOrderDate", pODate);
                                                                            orderMap.put("orderId", "");
                                                                            orderMap.put("orderStatus", "Not yet Delivered");
                                                                            orderMap.put("orderThrough", "paytm");

                                                                            orderRef.updateChildren(orderMap);

                                                                            Toast.makeText(UserConfirmActivity.this, "Order has been placed through PAYTM", Toast.LENGTH_SHORT).show();
                                                                            Intent i = new Intent(UserConfirmActivity.this, DashBoardActivity.class);
                                                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                            startActivity(i);
                                                                            finish();
                                                                            dialog.dismiss();
                                                                            // paymentDialog.dismiss();
                                                                            loadingdialog.dismiss();
                                                                            orderConfirmationLayout.setVisibility(View.INVISIBLE);
                                                                        }else{
                                                                            Toast.makeText(UserConfirmActivity.this, "Error: "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void networkNotAvailable() {
                                        Toast.makeText(UserConfirmActivity.this, "Network not available", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void clientAuthenticationFailed(String inErrorMessage) {
                                        Toast.makeText(UserConfirmActivity.this, "Error"+inErrorMessage.toString(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void someUIErrorOccurred(String inErrorMessage) {
                                        Toast.makeText(UserConfirmActivity.this, "Error"+inErrorMessage.toString(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                        Toast.makeText(UserConfirmActivity.this, "Error"+inErrorMessage.toString(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onBackPressedCancelTransaction() {
                                        Toast.makeText(UserConfirmActivity.this, "Transaction cancelled", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                        Toast.makeText(UserConfirmActivity.this, "Error"+inErrorMessage, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(UserConfirmActivity.this, "Error:"+ error, Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> paraMap = new HashMap<String, String>();
                        paraMap.put("MID", mId);
                        paraMap.put("ORDER_ID", orderId);
                        paraMap.put("CUST_ID", customerId);
                        paraMap.put("CHANNEL_ID", "WAP");
                        paraMap.put("TXN_AMOUNT", ttlPrice);
                        paraMap.put("WEBSITE","WEBSTAGING");
                        paraMap.put("INDUSTRY_TYPE_ID", "Retail");
                        paraMap.put("CALLBACK_URL",callBackUrl);
                        return  paraMap;
                    }
                };

                requestQueue.add(stringRequest);
            }
        });
    }

    private void ConfirmOrder() {
        dialog = new ProgressDialog(UserConfirmActivity.this);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.lodaing_dialog_loading);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // paymentDialog.dismiss();
        loadingdialog.dismiss();
        final String curTime, curDate;
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat date = new SimpleDateFormat("MMM dd,yyyy");
        curDate = date.format(cal.getTime());

        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
        curTime = time.format(cal.getTime());

        orderId = curDate+curTime;
        ouid = curDate+curTime;

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("User View").child("+91"+curPhone).child(curPhone);

        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderTime", curTime);
        orderMap.put("orderDate", curDate);
        orderMap.put("state", "In Progress");
        orderMap.put("totalAmount", ttlPrice);
        orderMap.put("userName", name.getText().toString());
        orderMap.put("phone", phone.getText().toString());
        orderMap.put("address", address.getText().toString());
        orderMap.put("city", city.getText().toString());
        orderMap.put("orderThrough", "cashOnDelivery");
        orderMap.put("paytmOrderDate", "");
        orderMap.put("orderId", orderId);
        orderMap.put("paytmOrderId", "");
        orderMap.put("orderStatus", "Not yet Delivered");

        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
                            .child("+91"+curPhone).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("Admin View").child("+91"+curPhone);

                                HashMap<String, Object> orderMap = new HashMap<>();
                                orderMap.put("orderTime", curTime);
                                orderMap.put("orderDate", curDate);
                                orderMap.put("state", "In Progress");
                                orderMap.put("totalAmount", ttlPrice);
                                orderMap.put("userName", name.getText().toString());
                                orderMap.put("phone", phone.getText().toString());
                                orderMap.put("address", address.getText().toString());
                                orderMap.put("city", city.getText().toString());
                                orderMap.put("orderThrough", "cashOnDelivery");
                                orderMap.put("paytmOrderDate", "");
                                orderMap.put("orderId", orderId);
                                orderMap.put("paytmOrderId", "");
                                orderMap.put("orderStatus", "Not yet Delivered");

                                orderRef.updateChildren(orderMap);

                                Toast.makeText(UserConfirmActivity.this, "Order has been submitted", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(UserConfirmActivity.this, DashBoardActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                                dialog.dismiss();
                                // paymentDialog.dismiss();
                                loadingdialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }
}
