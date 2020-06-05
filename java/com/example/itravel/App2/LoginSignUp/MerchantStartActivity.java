package com.example.itravel.App2.LoginSignUp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.itravel.App2.Admin.AdminLoginActivity;
import com.example.itravel.App2.Sellers.MerchantLoginActivity;
import com.example.itravel.App2.Sellers.MerchantSignUpActivity;
import com.example.itravel.App2.Sellers.SellerHomeActivity;
import com.example.itravel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MerchantStartActivity extends AppCompatActivity {
    private ProgressDialog loadingdialog;
    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_merchant_start);

        findViewById(R.id.merch_start_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MerchantStartActivity.super.onBackPressed();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void callLogin(View view){
        Intent i = new Intent(MerchantStartActivity.this, MerchantLoginActivity.class);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.merch_start_login),"trans_login");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MerchantStartActivity.this, pairs);
            startActivity(i, options.toBundle());
        }else{
            startActivity(i);
        }
    }

    public void callReg(View view){
        startActivity(new Intent(MerchantStartActivity.this, MerchantSignUpActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            loadingdialog = new ProgressDialog(this);
            loadingdialog.show();
            loadingdialog.setContentView(R.layout.loading_please_wait);
            loadingdialog.setCanceledOnTouchOutside(false);
            loadingdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            Intent i= new Intent(MerchantStartActivity.this, SellerHomeActivity.class);;
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            loadingdialog.dismiss();
        }
    }

    public void callAdmin(View view){
            startActivity(new Intent(MerchantStartActivity.this, AdminLoginActivity.class));
            finish();
    }
}
