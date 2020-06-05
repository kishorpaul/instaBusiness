package com.example.itravel.App2.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itravel.App2.Interface.itemClickListener;
import com.example.itravel.R;

public class OrderHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView orderId,through,amount,date,state;
    public itemClickListener itemClickListener;

    public OrderHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.item_user_order_date);
        amount = itemView.findViewById(R.id.item_user_order_amount);
        through = itemView.findViewById(R.id.item_user_order_through);
        orderId = itemView.findViewById(R.id.item_user_order_id);
        state = itemView.findViewById(R.id.item_user_order_state);
    }

    public void setItemClickListener(itemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

    }
}
