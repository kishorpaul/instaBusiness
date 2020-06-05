package com.example.itravel.App2.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.itravel.App2.Models.History;
import com.example.itravel.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminAllWishedListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_all_wished_list);

        recyclerView = findViewById(R.id.allfullfilled_recycler);
        back = findViewById(R.id.allfullfilled_Back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminAllWishedListActivity.this, AdminHomeActivity.class));
                finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().child("Fullfilled Orders");

        FirebaseRecyclerOptions<History> options = new FirebaseRecyclerOptions.Builder<History>()
                .setQuery(historyRef.child("Admin View"), History.class).build();

        FirebaseRecyclerAdapter<History, HistoryViewHolder> adapter = new FirebaseRecyclerAdapter<History, HistoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i, @NonNull History history) {
                historyViewHolder.hisId.setText(history.getShippedId());
                historyViewHolder.hisUserName.setText(history.getShippedUserName());
                historyViewHolder.hisAmount.setText("Price: "+history.getShippedTotalPrice());
                historyViewHolder.hisDate.setText(history.getShippedDate()+" | "+history.getShippedTime());
                historyViewHolder.hisState.setText("Status: "+history.getShippedState());
            }

            @NonNull
            @Override
            public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_history,parent,false);
                return new HistoryViewHolder(v);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView hisId,hisUserName,hisDate,hisAmount,hisState;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            hisId = itemView.findViewById(R.id.ite_admin_historyId);
            hisUserName = itemView.findViewById(R.id.ite_admin_historyUserName);
            hisAmount = itemView.findViewById(R.id.ite_admin_historyamount);
            hisDate = itemView.findViewById(R.id.ite_admin_historydate);
            hisState = itemView.findViewById(R.id.ite_admin_historyState);
        }
    }
}
