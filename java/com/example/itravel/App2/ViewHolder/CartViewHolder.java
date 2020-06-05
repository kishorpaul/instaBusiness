package com.example.itravel.App2.ViewHolder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itravel.App2.Interface.itemClickListener;
import com.example.itravel.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView image;
    public TextView pPrice,pName,pQuantity;
    public itemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.item_cart_pImage);
        pPrice = itemView.findViewById(R.id.item_cart_pPrice);
        pQuantity = itemView.findViewById(R.id.item_cart_pQuantity);
        pName = itemView.findViewById(R.id.item_cart_pName);
    }

    @Override
    public void onClick(View v) {

    }

    public void setOnClickListener(itemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
