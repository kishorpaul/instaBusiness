package com.example.itravel.App2.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.itravel.R;

public class SplashScreenActivity extends AppCompatActivity {
    private  static int SPLASH = 5000;
    ImageView image1;
    TextView t1;
    Animation sideAnim, bottomAnim;
    SharedPreferences onBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        t1 = findViewById(R.id.splash_text);
        image1 = findViewById(R.id.splash_image);

        sideAnim = AnimationUtils.loadAnimation(this,R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        image1.setAnimation(sideAnim);
        t1.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onBoard = getSharedPreferences("onBoard", MODE_PRIVATE);
                boolean newUser = onBoard.getBoolean("newUser", true);

                if (newUser) {
                    SharedPreferences.Editor editor = onBoard.edit();
                    editor.putBoolean("newUser", false);
                    editor.commit();

                    Intent i = new Intent(SplashScreenActivity.this, OnBoardActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(SplashScreenActivity.this, DashBoardActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH);
    }
}
