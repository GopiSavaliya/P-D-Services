package com.example.pdservices;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Activity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Activity extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View activityView;
    TextView activity_title;
    RecyclerView activity_recyclerView;
    ActivityRecyclerViewAdapter activityRecyclerViewAdapter;
    FirebaseFirestore fstore;
    String UserId;
    ArrayList<HashMap<String, Object>> arrayList;

    public Activity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Activity.
     */
    // TODO: Rename and change types and number of parameters
    public static Activity newInstance(String param1, String param2) {
        Activity fragment = new Activity();
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
        activityView = inflater.inflate(R.layout.fragment_activity, container, false);
        activity_title = activityView.findViewById(R.id.activity_title);
        activity_recyclerView = (RecyclerView)activityView.findViewById(R.id.activity_list);

        UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fstore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fstore.collection("scheduleRide").document(UserId+"Ride");
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                assert value != null;
                if(value.exists()) {
                    activity_title.setText("Activity");
                    HashMap<String, Object> scheduleRideMap = new HashMap<>();
                    scheduleRideMap.put("From", value.get("From"));
                    scheduleRideMap.put("To", value.get("To"));
                    scheduleRideMap.put("Date", value.get("Date"));
                    scheduleRideMap.put("Time", value.get("Time"));
                    scheduleRideMap.put("Charge", value.get("Charge"));
                    scheduleRideMap.put("NoOfPeople", value.get("NoOfPeople"));
                    scheduleRideMap.put("Weight", value.get("Weight"));
                    scheduleRideMap.put("Status", value.get("Status"));
                    scheduleRideMap.put("UserId", value.get("UserId"));

                    arrayList = new ArrayList<HashMap<String, Object>>();
                    arrayList.add(scheduleRideMap);

                    activityRecyclerViewAdapter = new ActivityRecyclerViewAdapter(arrayList, getContext());
                    activity_recyclerView.setAdapter(activityRecyclerViewAdapter);
                    activity_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }
        });

        /*documentReference = fstore.collection("carPool").document(UserId+"Ride");
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                assert value != null;
                if(value.exists()) {
                    activity_title.setText("Activity");
                    HashMap<String, Object> scheduleRideMap = new HashMap<>();
                    scheduleRideMap.put("From", value.get("From"));
                    scheduleRideMap.put("To", value.get("To"));
                    scheduleRideMap.put("Date", value.get("Date"));
                    scheduleRideMap.put("Time", value.get("Time"));
                    scheduleRideMap.put("NoOfPeople", value.get("NoOfPeople"));
                    scheduleRideMap.put("Status", value.get("Status"));
                    scheduleRideMap.put("Note", value.get("Note"));
                    scheduleRideMap.put("RequestFrom", value.get("RequestFrom"));
                    scheduleRideMap.put("RequestTo", value.get("RequestTo"));

                    arrayList.add(scheduleRideMap);

                    activityRecyclerViewAdapter = new ActivityRecyclerViewAdapter(arrayList, getContext());
                    activity_recyclerView.setAdapter(activityRecyclerViewAdapter);
                    activity_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }
        });*/


        return activityView;
    }
}