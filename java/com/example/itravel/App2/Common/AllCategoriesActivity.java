package com.example.itravel.App2.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.itravel.App2.ProductCategory.ProductCategoryActivity;
import com.example.itravel.R;

public class AllCategoriesActivity extends AppCompatActivity {
    ImageView back;
    RelativeLayout mobileRl,shoeRl,watchRl,glassRl,shirtRl,laptopRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_categories);

        back = findViewById(R.id.cat_back);
        laptopRl = findViewById(R.id.laptopRl);
        mobileRl = findViewById(R.id.mobileRl);
        shoeRl = findViewById(R.id.shoeRl);
        watchRl = findViewById(R.id.watchRl);
        glassRl = findViewById(R.id.glassRl);
        shirtRl = findViewById(R.id.shirtRl);

        laptopRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllCategoriesActivity.this, ProductCategoryActivity.class);
                i.putExtra("cat", "Laptop");
                startActivity(i);
            }
        });

        mobileRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllCategoriesActivity.this, ProductCategoryActivity.class);
                i.putExtra("cat", "Mobile");
                startActivity(i);
            }
        });

        shoeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllCategoriesActivity.this, ProductCategoryActivity.class);
                i.putExtra("cat", "Shoe");
                startActivity(i);
            }
        });

        watchRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllCategoriesActivity.this, ProductCategoryActivity.class);
                i.putExtra("cat", "Watch");
                startActivity(i);
            }
        });

        shirtRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllCategoriesActivity.this, ProductCategoryActivity.class);
                i.putExtra("cat", "Shirt");
                startActivity(i);
            }
        });

        glassRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllCategoriesActivity.this, ProductCategoryActivity.class);
                i.putExtra("cat", "Glass");
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllCategoriesActivity.super.onBackPressed();
            }
        });
    }
}
