package com.example.pdservices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestCPAdapter extends RecyclerView.Adapter<RequestCPAdapter.RequestCPViewHolder> {

    ArrayList<HashMap<String, Object>> requestCPList;
    Context context;
    public static String selectedUserId = null;

    public RequestCPAdapter(ArrayList<HashMap<String, Object>> requestCPList, Context context) {
        this.requestCPList = requestCPList;
        this.context = context;
    }

    @NotNull
    @Override
    public RequestCPViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_people, parent, false);
        RequestCPViewHolder viewHolder = new RequestCPViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RequestCPAdapter.RequestCPViewHolder holder, int position) {
        holder.radioButton.setText(requestCPList.get(position).get("Name").toString());
        holder.charge.setText(requestCPList.get(position).get("Charge").toString());
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedUserId = requestCPList.get(position).get("UserId").toString();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestCPList.size();
    }

    class RequestCPViewHolder extends RecyclerView.ViewHolder {

        RadioButton radioButton;
        TextView charge;
        View view;

        public RequestCPViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButton);
            charge = itemView.findViewById(R.id.charge);
            view = itemView;
        }
    }
}
