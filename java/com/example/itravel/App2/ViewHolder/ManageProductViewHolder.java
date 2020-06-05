package com.example.itravel.App2.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itravel.App2.Interface.itemClickListener;
import com.example.itravel.R;

public class ManageProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
    public TextView pName, pDesc, pPrice;
    public ImageView pImage;
    public itemClickListener itemClickListener;

    public ManageProductViewHolder(@NonNull View itemView) {
        super(itemView);

        pName = itemView.findViewById(R.id.item_manage_pName);
        pDesc = itemView.findViewById(R.id.item_manage_pDesc);
        pPrice = itemView.findViewById(R.id.item_manage_pPrice);
        pImage = itemView.findViewById(R.id.item_manage_pImage);
    }

    @Override
    public void onClick(View v) {
    }

    public void setItemClickListener(itemClickListener itemClickListener)
    {
        this. itemClickListener =  itemClickListener;
    }
}
