package com.example.itravel.App2.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itravel.App2.Interface.itemClickListener;
import com.example.itravel.R;

public class UserSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView image;
    public TextView name,desc,price;
    public itemClickListener itemClickListener;

    public UserSearchViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.item_search_image);
        name = itemView.findViewById(R.id.item_search_name);
        desc = itemView.findViewById(R.id.item_search_desc);
        price = itemView.findViewById(R.id.item_search_price);
    }

    private void setOnClickListener(itemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

    }
}
