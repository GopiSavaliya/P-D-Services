package com.example.pdservices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class RequestCarPool2 extends AppCompatActivity {

    HashMap<String, Object> carPoolMap, requestMap;
    TextView car_pool_result;
    String userId;
    RecyclerView carpool_requests_list;
    RequestCPAdapter requestCPAdapter;
    FirebaseFirestore fStore;
    Map<String, Object> map;
    ArrayList<HashMap<String, Object>> arrayList;
    Button car_pool_cancel, car_pool_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_carpool2);

        car_pool_cancel = findViewById(R.id.car_pool_cancel);
        car_pool_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        Intent intent =getIntent();
        carPoolMap = (HashMap<String, Object>) intent.getSerializableExtra("carPoolMap");

        carpool_requests_list = findViewById(R.id.carpool_requests_list);
        car_pool_result = findViewById(R.id.car_pool_result);

        arrayList = new ArrayList<HashMap<String, Object>>();
        fStore = FirebaseFirestore.getInstance();
        fStore.collection("scheduleRide").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
               if(task.isSuccessful()) {
                   for(QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())) {
                       map = document.getData();
                       if(map.get("From").toString().equalsIgnoreCase(carPoolMap.get("From").toString())) {
                           if(map.get("To").toString().equalsIgnoreCase(carPoolMap.get("To").toString())) {
                               if(map.get("Date").toString().equalsIgnoreCase(carPoolMap.get("Date").toString())) {
                                   car_pool_result.setText("Results");
                                   requestMap = new HashMap<>();
                                   requestMap.put("Charge", map.get("Charge").toString());
                                   userId = map.get("UserId").toString();
                                   requestMap.put("UserId", userId);
                                   arrayList.add(requestMap);
                                   requestCPAdapter = new RequestCPAdapter(arrayList, RequestCarPool2.this);
                                   carpool_requests_list.setAdapter(requestCPAdapter);
                                   carpool_requests_list.setLayoutManager(new LinearLayoutManager(RequestCarPool2.this));
                                   FirebaseFirestore store = FirebaseFirestore.getInstance();
                                   DocumentReference documentReference = store.collection("users").document(userId);
                                   documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                       @Override
                                       public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                                           requestMap.put("Name", value.get("Name"));

                                       }
                                   });
                               }
                           }
                       }
                   }
               }
            }
        });
        car_pool_request = findViewById(R.id.car_pool_request);
        car_pool_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RequestCPAdapter.selectedUserId!=null) {
                    carPoolMap.put("RequestTo", RequestCPAdapter.selectedUserId);
                    fStore = FirebaseFirestore.getInstance();
                    userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    carPoolMap.put("RequestFrom", userId);
                    carPoolMap.put("Status", "pending");
                    DocumentReference documentReference = fStore.collection("carPool").document(userId+"RequestCarPool");
                    documentReference.set(carPoolMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RequestCarPool2.this, "Request for CarPool is sent", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RequestCarPool2.this, MainActivity.class));
                        }
                    });
                }
            }
        });
    }
}