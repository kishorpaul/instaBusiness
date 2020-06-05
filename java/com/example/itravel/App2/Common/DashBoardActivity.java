package com.example.itravel.App2.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.itravel.App2.Prevalent.Prevalent;
import com.example.itravel.App2.ProductCategory.ProductCategoryActivity;
import com.example.itravel.App2.Users.UserCartActivity;
import com.example.itravel.App2.Users.UserLoginActivity;
import com.example.itravel.App2.Users.UserOrderActivity;
import com.example.itravel.App2.Users.UserProfileActivity;
import com.example.itravel.App2.Users.UserSearchActivity;
import com.example.itravel.App2.LoginSignUp.MerchantStartActivity;
import com.example.itravel.App2.Models.Products;
import com.example.itravel.App2.Users.ProductDetailActivity;
import com.example.itravel.App2.ViewHolder.CatViewHolder;
import com.example.itravel.App2.ViewHolder.FeatureViewHolder;
import com.example.itravel.App2.ViewHolder.MVViewHolder;
import com.example.itravel.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView name, email;
    Button logout;
    RecyclerView recyclerView1, recyclerView2, recyclerView3;
    RecyclerView.Adapter adapter1, adapter2, adapter3;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon, profileImage;
    static final float END_SCALE = 0.7f;
    LinearLayout contentView;
    DatabaseReference pRef,uRef;
    RelativeLayout searchRL;
    TextView nav_profileFullName;
    LinearLayout shoell, mobilell,watchll,glassll;
    View hView;
    String curPh;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dash_board);

        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView3 = findViewById(R.id.recycler_view3);
        navigationView = findViewById(R.id.nav_layout);
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.nav_drawer_btn);
        contentView = findViewById(R.id.content);
        profileImage = findViewById(R.id.dashBoard_profileImage);
        searchRL = findViewById(R.id.search_rl);
        glassll = findViewById(R.id.glasses_ll);
        shoell = findViewById(R.id.shoe_ll);
        mobilell = findViewById(R.id.mobile_ll);
        watchll = findViewById(R.id.watch_ll);

        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setContentView(R.layout.loading_loading);
        dialog.setCanceledOnTouchOutside(false);

        Paper.init(this);
        curPh = Paper.book().read(Prevalent.UserPhoneKey);

        glassll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoardActivity.this, ProductCategoryActivity.class);
                i.putExtra("cat", "Glass");
                startActivity(i);
            }
        });

        shoell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoardActivity.this, ProductCategoryActivity.class);
                i.putExtra("cat", "Shoe");
                startActivity(i);
            }
        });

        watchll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoardActivity.this, ProductCategoryActivity.class);
                i.putExtra("cat", "Watch");
                startActivity(i);
            }
        });

        mobilell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoardActivity.this, ProductCategoryActivity.class);
                i.putExtra("cat", "Mobile");
                startActivity(i);
            }
        });

        searchRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this, UserSearchActivity.class));
            }
        });

        pRef = FirebaseDatabase.getInstance().getReference().child("Products");

        navDrawer();

        recyclerView1();
        recyclerView2();
        recyclerView3();
//        name = findViewById(R.id.dash_name);
//        email = findViewById(R.id.dash_email);
//        logout = findViewById(R.id.dash_logout);
//
//        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if (signInAccount != null){
//            name.setText(signInAccount.getDisplayName());
//            email.setText(signInAccount.getEmail());
//        }
//
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(DashBoardActivity.this, LoginActivity.class));
//                finish();
//            }
//        });
    }

    private void navDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        hView = navigationView.getHeaderView(0);
        nav_profileFullName = (TextView)hView.findViewById(R.id.nav_profileFullName);

        nav_profileFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curPh!=null){
                    Intent i = new Intent(DashBoardActivity.this, UserProfileActivity.class);
                    i.putExtra("phone", curPh);
                    startActivity(i);
                }else{
                    Toast.makeText(DashBoardActivity.this, "Please login in to your account.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        uRef = FirebaseDatabase.getInstance().getReference().child("Users");

        if (curPh!=null){
            uRef.child("+91"+curPh).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String uName = dataSnapshot.child("uName").getValue().toString();
                        nav_profileFullName.setText("Hello, "+uName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        animateNavBar();
    }

    private void animateNavBar() {
        // drawerLayout.setScrimColor(getResources().getColor(R.color.colorPrimary));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                final float diffScaleOffSet = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaleOffSet;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                final float xoffset = drawerView.getWidth() * slideOffset;
                final float xoffsetDiff = contentView.getWidth() * diffScaleOffSet / 2;
                final float xTranslarte = xoffset - xoffsetDiff;
                contentView.setTranslationX(xTranslarte);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void recyclerView3()   {
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(pRef.orderByChild("verifyStat").equalTo("approved"), Products.class).build();

        FirebaseRecyclerAdapter<Products, MVViewHolder> adapter = new FirebaseRecyclerAdapter<Products, MVViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MVViewHolder featureViewHolder, int i, @NonNull final Products products) {
                featureViewHolder.pName.setText(products.getpName());
                featureViewHolder.pPrice.setText(products.getpPrice()+"$");
                featureViewHolder.pDesc.setText(products.getpDesc());

                Glide.with(DashBoardActivity.this).load(products.getpImageUrl()).placeholder(R.drawable.google).into(featureViewHolder.image);

                featureViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(DashBoardActivity.this, ProductDetailActivity.class);
                        i.putExtra("pid", products.getPid());
                        i.putExtra("pImageUrl", products.getpImageUrl());
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public MVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_viewed_item, parent, false);
                return new MVViewHolder(v);
            }
        };
        recyclerView3.setAdapter(adapter);
        adapter.startListening();
        dialog.dismiss();
//        ArrayList<CategoryModel> categoryModel = new ArrayList<>();
//        categoryModel.add(new CategoryModel(R.drawable.android, "isojd foidfj diovjfiodjvifojv ijv oijfsdj vfj/", "Android"));
//        categoryModel.add(new CategoryModel(R.drawable.google, "isojd foidfj diovjfiodjvifojv ijv oijfsdj vfj/", "Android"));
//        categoryModel.add(new CategoryModel(R.drawable.android, "isojd foidfj diovjfiodjvifojv ijv oijfsdj vfj/", "Android"));
//        categoryModel.add(new CategoryModel(R.drawable.google, "isojd foidfj diovjfiodjvifojv ijv oijfsdj vfj/", "Android"));
//        categoryModel.add(new CategoryModel(R.drawable.android, "isojd foidfj diovjfiodjvifojv ijv oijfsdj vfj/", "Android"));
//        categoryModel.add(new CategoryModel(R.drawable.google, "isojd foidfj diovjfiodjvifojv ijv oijfsdj vfj/", "Android"));
//        categoryModel.add(new CategoryModel(R.drawable.android, "isojd foidfj diovjfiodjvifojv ijv oijfsdj vfj/", "Android"));
//        categoryModel.add(new CategoryModel(R.drawable.google, "isojd foidfj diovjfiodjvifojv ijv oijfsdj vfj/", "Android"));
//        categoryModel.add(new CategoryModel(R.drawable.android, "isojd foidfj diovjfiodjvifojv ijv oijfsdj vfj/", "Android"));
//
//        adapter3 = new CaterogyAdapter(categoryModel);
//        recyclerView3.setAdapter(adapter3);
//
//        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffeff400, 0xffeff600});
    }

    private void recyclerView2() {
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(pRef.orderByChild("verifyStat").equalTo("approved"), Products.class).build();

        FirebaseRecyclerAdapter<Products, CatViewHolder> adapter = new FirebaseRecyclerAdapter<Products, CatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CatViewHolder catViewHolder, int i, @NonNull final Products products) {
                catViewHolder.pName.setText(products.getpName());
                catViewHolder.pPrice.setText(products.getpPrice()+"$");
                catViewHolder.pDesc.setText(products.getpDesc());

                Glide.with(DashBoardActivity.this).load(products.getpImageUrl()).placeholder(R.drawable.google).into(catViewHolder.image);

                catViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(DashBoardActivity.this, ProductDetailActivity.class);
                        i.putExtra("pid", products.getPid());
                        i.putExtra("pImageUrl", products.getpImageUrl());
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
                return new CatViewHolder(v);
            }
        };
        recyclerView2.setAdapter(adapter);
        adapter.startListening();
        dialog.dismiss();
//        ArrayList<MostViewedModel> mostViewedModel = new ArrayList<>();
//        mostViewedModel.add(new MostViewedModel(R.drawable.android, "Android"));
//        mostViewedModel.add(new MostViewedModel(R.drawable.google, "Google"));
//        mostViewedModel.add(new MostViewedModel(R.drawable.android, "Android"));
//        mostViewedModel.add(new MostViewedModel(R.drawable.google, "Google"));
//        mostViewedModel.add(new MostViewedModel(R.drawable.android, "Android"));
//        mostViewedModel.add(new MostViewedModel(R.drawable.google, "Google"));
//        mostViewedModel.add(new MostViewedModel(R.drawable.android, "Android"));
//        mostViewedModel.add(new MostViewedModel(R.drawable.google, "Google"));
//
//        adapter2 = new MostViewedAdapter(mostViewedModel);
//        recyclerView2.setAdapter(adapter2);
    }

    private void recyclerView1() {
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(pRef.orderByChild("verifyStat").equalTo("approved"), Products.class).build();

       FirebaseRecyclerAdapter<Products, FeatureViewHolder> adapter = new FirebaseRecyclerAdapter<Products, FeatureViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull FeatureViewHolder featureViewHolder, int i, @NonNull final Products products) {
               featureViewHolder.pName.setText(products.getpName());
               featureViewHolder.pPrice.setText(products.getpPrice()+"$");
               featureViewHolder.pDesc.setText(products.getpDesc());

               Glide.with(DashBoardActivity.this).load(products.getpImageUrl()).placeholder(R.drawable.google).into(featureViewHolder.image);

               featureViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent i = new Intent(DashBoardActivity.this, ProductDetailActivity.class);
                       i.putExtra("pid", products.getPid());
                       i.putExtra("pImageUrl", products.getpImageUrl());
                       startActivity(i);
                   }
               });
           }

           @NonNull
           @Override
           public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
              View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_item, parent, false);
              return new FeatureViewHolder(v);
           }
       };
       recyclerView1.setAdapter(adapter);
       adapter.startListening();
       dialog.dismiss();
//        ArrayList<FeaturedModel> featuredModel = new ArrayList<>();
//        featuredModel.add(new FeaturedModel(R.drawable.android, "Android", "All the best in the city is here and no where else to find"));
//        featuredModel.add(new FeaturedModel(R.drawable.google, "Google", "All the best in the city is here and no where else to find"));
//        featuredModel.add(new FeaturedModel(R.drawable.android, "Android", "All the best in the city is here and no where else to find"));
//        featuredModel.add(new FeaturedModel(R.drawable.google, "Google", "All the best in the city is here and no where else to find"));
//        featuredModel.add(new FeaturedModel(R.drawable.android, "Android", "All the best in the city is here and no where else to find"));
//        featuredModel.add(new FeaturedModel(R.drawable.google, "Google", "All the best in the city is here and no where else to find"));
//
//        adapter1 = new FeaturedAdapter(featuredModel);
//        recyclerView1.setAdapter(adapter1);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_search:
                startActivity(new Intent(DashBoardActivity.this, AllCategoriesActivity.class));
                break;
            case R.id.nav_home:
               // startActivity(new Intent(DashBoardActivity.this, DashBoardActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_profile:
                startActivity(new Intent(DashBoardActivity.this, UserLoginActivity.class));
                break;
            case R.id.nav_cart:
                startActivity(new Intent(DashBoardActivity.this, UserCartActivity.class));
                break;
            case R.id.nav_Orders:
                startActivity(new Intent(DashBoardActivity.this, UserOrderActivity.class));
                break;
            case R.id.nav_mobile:
                Intent i = new Intent(DashBoardActivity.this, ProductCategoryActivity.class);
                i.putExtra("cat", "Mobile");
                startActivity(i);
                break;
            case R.id.nav_shoe:
                Intent i1 = new Intent(DashBoardActivity.this, ProductCategoryActivity.class);
                i1.putExtra("cat", "Shoe");
                startActivity(i1);
                break;
            case R.id.nav_watches:
                Intent i2 = new Intent(DashBoardActivity.this, ProductCategoryActivity.class);
                i2.putExtra("cat", "Watch");
                startActivity(i2);
                break;
            case R.id.nav_glasses:
                Intent i3 = new Intent(DashBoardActivity.this, ProductCategoryActivity.class);
                i3.putExtra("cat", "Glass");
                startActivity(i3);
                break;
            case R.id.nav_hats:
                Intent i4 = new Intent(DashBoardActivity.this, ProductCategoryActivity.class);
                i4.putExtra("cat", "Shirt");
                startActivity(i4);
                break;

        }
        return true;
    }

    public  void callMerchant(View view){
        startActivity(new Intent(DashBoardActivity.this, MerchantStartActivity.class));
    }

    public void callCategory(View view){
        startActivity(new Intent(DashBoardActivity.this, AllCategoriesActivity.class));
    }
}
