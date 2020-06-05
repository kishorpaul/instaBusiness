package com.example.itravel.App2.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itravel.App2.Interface.itemClickListener;
import com.example.itravel.R;


public class SellerProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView prodName, prodDesc,prodPrice,pStat;
    public ImageView prodImage;
    public itemClickListener itemClickListener;

    public SellerProductViewHolder(@NonNull View itemView) {
        super(itemView);
        prodImage = itemView.findViewById(R.id.item_seller_pImage);
        prodName = itemView.findViewById(R.id.item_seller_pName);
        prodDesc = itemView.findViewById(R.id.item_seller_pDesc);
        prodPrice = itemView.findViewById(R.id.item_seller_pPrice);
        pStat = itemView.findViewById(R.id.item_seller_pStatus);
    }

    public void setItemClickListener(itemClickListener itemClickListener)
    {
        this. itemClickListener =  itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.OnClick(v,getAdapterPosition(),false);
    }
}
