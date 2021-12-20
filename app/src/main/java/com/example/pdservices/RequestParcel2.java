package com.example.pdservices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class RequestParcel2 extends AppCompatActivity {

    HashMap<String, Object> parcelMap, requestMap;
    Button parcel_cancel, parcel_request;
    TextView parcel_result;
    RecyclerView parcel_requests_list;
    ArrayList<HashMap<String, Object>> arrayList;
    FirebaseFirestore fStore;
    Map<String, Object> map;
    String userId;
    RequestCPAdapter requestCPAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_parcel2);

        parcel_cancel = findViewById(R.id.parcel_cancel);
        parcel_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        Intent intent =getIntent();
        parcelMap = (HashMap<String, Object>) intent.getSerializableExtra("parcelMap");

        parcel_result = findViewById(R.id.parcel_result);
        parcel_requests_list = findViewById(R.id.parcel_requests_list);

        arrayList = new ArrayList<HashMap<String, Object>>();
        fStore = FirebaseFirestore.getInstance();
        fStore.collection("scheduleRide").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())) {
                        map = document.getData();
                        if(map.get("From").toString().equalsIgnoreCase(parcelMap.get("From").toString())) {
                            if (map.get("To").toString().equalsIgnoreCase(parcelMap.get("To").toString())) {
                                if (map.get("Date").toString().equalsIgnoreCase(parcelMap.get("Date").toString())) {
                                    parcel_result.setText("Results");
                                    requestMap = new HashMap<>();
                                    requestMap.put("Charge", map.get("Charge").toString());
                                    userId = map.get("UserId").toString();
                                    requestMap.put("UserId", userId);
                                    FirebaseFirestore store = FirebaseFirestore.getInstance();
                                    DocumentReference documentReference = store.collection("users").document(userId);
                                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                                            requestMap.put("Name", value.get("Name"));
                                            arrayList.add(requestMap);
                                            requestCPAdapter = new RequestCPAdapter(arrayList, RequestParcel2.this);
                                            parcel_requests_list.setAdapter(requestCPAdapter);
                                            parcel_requests_list.setLayoutManager(new LinearLayoutManager(RequestParcel2.this));
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
        });
        parcel_request = findViewById(R.id.parcel_request);
        parcel_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RequestCPAdapter.selectedUserId!=null) {
                    parcelMap.put("RequestTo", RequestCPAdapter.selectedUserId);
                    fStore = FirebaseFirestore.getInstance();
                    userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    parcelMap.put("RequestFrom", userId);
                    parcelMap.put("Status", "pending");
                    DocumentReference documentReference = fStore.collection("parcel").document(userId+"RequestParcel");
                    documentReference.set(parcelMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            Toast.makeText(RequestParcel2.this, "Request for Parcel is sent", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RequestParcel2.this, MainActivity.class));
                        }
                    });
                }
            }
        });
    }
}