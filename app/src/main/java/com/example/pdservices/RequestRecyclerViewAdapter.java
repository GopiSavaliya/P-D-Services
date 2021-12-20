package com.example.pdservices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestRecyclerViewAdapter extends RecyclerView.Adapter<RequestRecyclerViewAdapter.RequestViewHolder>{

    ArrayList<HashMap<String, Object>> requestList;
    Context context;

    public RequestRecyclerViewAdapter(ArrayList<HashMap<String, Object>> requestList, Context context) {
        this.requestList = requestList;
        this.context = context;
    }

    @NotNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.request_row, parent, false);
        RequestRecyclerViewAdapter.RequestViewHolder viewHolder = new RequestRecyclerViewAdapter.RequestViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RequestRecyclerViewAdapter.RequestViewHolder holder, int position) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        String userId = requestList.get(position).get("RequestFrom").toString();
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                assert value != null;
                FirebaseFirestore store = FirebaseFirestore.getInstance();
                DocumentReference documentReference = store.collection("users").document(userId);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        holder.request_name.setText(value.get("Name").toString() + " requested for " + requestList.get(position).get("RequestFor").toString() + " service");
                    }
                });
            }
        });

        holder.request_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                if(requestList.get(position).get("RequestFor").toString().equals("carpool")) {
                    fStore.collection("carPool").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Map<String, Object> map;
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    map = document.getData();
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    if (map.get("RequestTo").toString().equals(userId)) {
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("From", map.get("From").toString());
                                        hashMap.put("To", map.get("To").toString());
                                        hashMap.put("Date", map.get("Date").toString());
                                        hashMap.put("Time", map.get("Time").toString());
                                        hashMap.put("NoOfPeople", map.get("NoOfPeople").toString());
                                        hashMap.put("Note", map.get("Note").toString());
                                        hashMap.put("RequestTo", map.get("RequestTo").toString());
                                        hashMap.put("RequestFrom", map.get("RequestFrom").toString());
                                        hashMap.put("Status", "waiting");
                                        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                                        DocumentReference documentReference = fStore.collection("carPool").document(map.get("RequestFrom").toString()+"RequestCarPool");
                                        documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                requestList.clear();
                                                hashMap.put("RequestFor", "carPool");
                                                requestList.add(hashMap);
                                                Toast.makeText(context, "Request Accepted", Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
                } else if(requestList.get(position).get("RequestFor").toString().equals("parcel")) {
                    fStore.collection("parcel").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Map<String, Object> map;
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    map = document.getData();
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    if (map.get("RequestTo").toString().equals(userId)) {
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("From", map.get("From").toString());
                                        hashMap.put("To", map.get("To").toString());
                                        hashMap.put("Date", map.get("Date").toString());
                                        hashMap.put("Time", map.get("Time").toString());
                                        hashMap.put("Weight", map.get("Weight").toString());
                                        hashMap.put("Note", map.get("Note").toString());
                                        hashMap.put("RequestTo", map.get("RequestTo").toString());
                                        hashMap.put("RequestFrom", map.get("RequestFrom").toString());
                                        hashMap.put("Status", "waiting");
                                        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                                        DocumentReference documentReference = fStore.collection("parcel").document(map.get("RequestFrom").toString()+"RequestParcel");
                                        documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                requestList.clear();
                                                hashMap.put("RequestFor", "parcel");
                                                requestList.add(hashMap);
                                                Toast.makeText(context, "Request Accepted", Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

        holder.request_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                if(requestList.get(position).get("RequestFor").toString().equals("carpool")) {
                    fStore.collection("carPool").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Map<String, Object> map;
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    map = document.getData();
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    if (map.get("RequestTo").toString().equals(userId)) {
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("From", map.get("From").toString());
                                        hashMap.put("To", map.get("To").toString());
                                        hashMap.put("Date", map.get("Date").toString());
                                        hashMap.put("Time", map.get("Time").toString());
                                        hashMap.put("NoOfPeople", map.get("NoOfPeople").toString());
                                        hashMap.put("Note", map.get("Note").toString());
                                        hashMap.put("RequestTo", map.get("RequestTo").toString());
                                        hashMap.put("RequestFrom", map.get("RequestFrom").toString());
                                        hashMap.put("Status", "rejected");
                                        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                                        DocumentReference documentReference = fStore.collection("carPool").document(map.get("RequestFrom").toString()+"RequestCarPool");
                                        documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                requestList.clear();
                                                hashMap.put("RequestFor", "carPool");
                                                requestList.add(hashMap);
                                                Toast.makeText(context, "Request Rejected", Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
                } else if(requestList.get(position).get("RequestFor").toString().equals("parcel")) {
                    fStore.collection("parcel").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Map<String, Object> map;
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    map = document.getData();
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    if (map.get("RequestTo").toString().equals(userId)) {
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("From", map.get("From").toString());
                                        hashMap.put("To", map.get("To").toString());
                                        hashMap.put("Date", map.get("Date").toString());
                                        hashMap.put("Time", map.get("Time").toString());
                                        hashMap.put("Weight", map.get("Weight").toString());
                                        hashMap.put("Note", map.get("Note").toString());
                                        hashMap.put("RequestTo", map.get("RequestTo").toString());
                                        hashMap.put("RequestFrom", map.get("RequestFrom").toString());
                                        hashMap.put("Status", "rejected");
                                        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                                        DocumentReference documentReference = fStore.collection("parcel").document(map.get("RequestFrom").toString()+"RequestParcel");
                                        documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                requestList.clear();
                                                hashMap.put("RequestFor", "parcel");
                                                requestList.add(hashMap);
                                                Toast.makeText(context, "Request Rejected", Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView request_name;
        Button request_confirm, request_cancel;
        View view;

        public RequestViewHolder(@NotNull View itemView) {
            super(itemView);
            request_name = itemView.findViewById(R.id.request_name);
            request_confirm = itemView.findViewById(R.id.request_confirm);
            request_cancel = itemView.findViewById(R.id.request_cancel);
            view = itemView;
        }
    }
}
