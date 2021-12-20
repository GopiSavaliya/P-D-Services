package com.example.pdservices;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Requests#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Requests extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View requestView;
    RecyclerView requests_list;
    TextView request_title;
    ArrayList<HashMap<String, Object>> arrayList;
    FirebaseFirestore fStore;
    Map<String, Object> map;
    HashMap<String, Object> hashMap;
    String userId;
    RequestRecyclerViewAdapter requestRecyclerViewAdapter;

    public Requests() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Requests.
     */
    // TODO: Rename and change types and number of parameters
    public static Requests newInstance(String param1, String param2) {
        Requests fragment = new Requests();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requestView = inflater.inflate(R.layout.fragment_requests, container, false);

        request_title = requestView.findViewById(R.id.request_title);
        arrayList = new ArrayList<HashMap<String, Object>>();
        requests_list = (RecyclerView)requestView.findViewById(R.id.requests_list);
        fStore = FirebaseFirestore.getInstance();
        fStore.collection("carPool").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())) {
                        map = document.getData();
                        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        if(map.get("RequestTo").toString().equals(userId) && map.get("Status").toString().equals("pending")) {
                            request_title.setText("Requests");
                            hashMap = (HashMap<String, Object>)map;
                            hashMap.put("RequestFor","carpool");
                            arrayList.add(hashMap);
                            requestRecyclerViewAdapter = new RequestRecyclerViewAdapter(arrayList, getContext());
                            requests_list.setAdapter(requestRecyclerViewAdapter);
                            requests_list.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }
                }
            }
        });
        fStore = FirebaseFirestore.getInstance();
        fStore.collection("parcel").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())) {
                        map = document.getData();
                        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        if(map.get("RequestTo").toString().equals(userId) && map.get("Status").toString().equals("pending")) {
                            request_title.setText("Requests");
                            hashMap = (HashMap<String, Object>)map;
                            hashMap.put("RequestFor","parcel");
                            arrayList.add(hashMap);
                            requestRecyclerViewAdapter = new RequestRecyclerViewAdapter(arrayList, getContext());
                            requests_list.setAdapter(requestRecyclerViewAdapter);
                            requests_list.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }
                }
            }
        });
        return requestView;
    }
}