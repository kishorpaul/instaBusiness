package com.example.itravel.App1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.itravel.R;

public class SliderAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context mContext) {
        this.mContext = mContext;
    }

    int[] images = {
            R.drawable.cash02,
            R.drawable.cash03,
            R.drawable.cash01,
            R.drawable.cash02
    };

    int[] heading = {
            R.string.second_sTitle,
            R.string.first_sTitle,
            R.string.third_sTitle,
            R.string.fourth_sTitle
    };

    int[] desc = {
          R.string.first_desc,
          R.string.second_desc,
          R.string.third_desc,
          R.string.fourth_desc
    };

    @Override
    public int getCount() {
        return heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.silde_layout, container, false);

        ImageView imageView = v.findViewById(R.id.slide1_image);
        TextView title = v.findViewById(R.id.slide1_text1);
        TextView desc1 = v.findViewById(R.id.slide1_text2);

        imageView.setImageResource(images[position]);
        title.setText(heading[position]);
        desc1.setText(desc[position]);

        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
