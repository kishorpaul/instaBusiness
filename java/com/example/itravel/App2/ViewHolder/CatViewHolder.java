package com.example.itravel.App2.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itravel.App2.Interface.itemClickListener;
import com.example.itravel.R;

public class CatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView image;
    public TextView pName, pPrice, pDesc;
    public com.example.itravel.App2.Interface.itemClickListener itemClickListener;

    public CatViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.cat_image);
        pPrice = itemView.findViewById(R.id.cat_price);
        pDesc = itemView.findViewById(R.id.cat_desc);
        pName = itemView.findViewById(R.id.cat_name);
    }

    public void setItemClickListener(itemClickListener itemClickListener)
    {
        this. itemClickListener =  itemClickListener;
    }

    @Override
    public void onClick(View v) {

    }
}
