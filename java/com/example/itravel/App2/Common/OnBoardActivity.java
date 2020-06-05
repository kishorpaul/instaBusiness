package com.example.itravel.App2.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.itravel.App1.Adapters.SliderAdapter;
import com.example.itravel.R;

public class OnBoardActivity extends AppCompatActivity {
    ViewPager viewPager;
    RelativeLayout relativeLayout;
    LinearLayout dots;
    SliderAdapter sliderAdapter;
    TextView[] textDots;
    Button started, skipbtn, nextbtn;
    Animation animation;
    int curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_board);

        viewPager = findViewById(R.id.view_pager);
        dots = findViewById(R.id.ll1);
        started = findViewById(R.id.getStartedBtn);
        skipbtn = findViewById(R.id.skip_btn);
        nextbtn = findViewById(R.id.nextBtn);

        sliderAdapter = new SliderAdapter(this);

        viewPager.setAdapter(sliderAdapter);

        addDots(0);
        viewPager.addOnPageChangeListener(listener);
    }

    public void skip(View view){
        Intent i = new Intent(OnBoardActivity.this, DashBoardActivity.class);
        finish();
    }

    public  void next(View view){
        viewPager.setCurrentItem(curr+1);
    }

    private void addDots(int position){
        textDots = new TextView[4];
        dots.removeAllViews();
        for (int i =0;i<textDots.length; i++){
            textDots[i] = new TextView(this);
            textDots[i].setText(Html.fromHtml("&#8226;"));
            textDots[i].setTextSize(35);

            dots.addView(textDots[i]);
        }
        if (textDots.length>0){
            textDots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            curr=position;
            if (position == 0){
                started.setVisibility(View.INVISIBLE);
            }else if(position == 1){
                started.setVisibility(View.INVISIBLE);
            }else if(position == 2){
                started.setVisibility(View.INVISIBLE);
            }else if(position == 3){
                animation = AnimationUtils.loadAnimation(OnBoardActivity.this,R.anim.side_anim);
                started.setAnimation(animation);
                started.setVisibility(View.VISIBLE);
                nextbtn.setVisibility(View.INVISIBLE);
                skipbtn.setVisibility(View.INVISIBLE);
                started.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(OnBoardActivity.this, DashBoardActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                });
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
