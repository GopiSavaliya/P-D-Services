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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ActivityRecyclerViewAdapter extends RecyclerView.Adapter {

    ArrayList<HashMap<String, Object>> scheduleRideList;
    Context context;
    private FirebaseFirestore fStore;
    private String UserId;

    public ActivityRecyclerViewAdapter(ArrayList<HashMap<String, Object>> scheduleRideList, Context context) {
        this.scheduleRideList = scheduleRideList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(Objects.requireNonNull(scheduleRideList.get(position).get("Status")).toString().equals("Start")){
            return 1;
        } else if(Objects.requireNonNull(scheduleRideList.get(position).get("Status")).toString().equals("CarMode")){
            return 2;
        } else if(Objects.requireNonNull(scheduleRideList.get(position).get("Status")).toString().equals("Reached")) {
            return 3;
        }
        return 0;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View ride_view;

        if(viewType == 1) {
            ride_view = inflater.inflate(R.layout.ride_start, parent, false);
            return new RideStartViewHolder(ride_view);
        } else if(viewType == 2) {
            ride_view = inflater.inflate(R.layout.ride_car_mode, parent, false);
            return new RideCarModeViewHolder(ride_view);
        } else if(viewType == 3) {
            ride_view = inflater.inflate(R.layout.ride_reached, parent, false);
            return new RideReachedViewHolder(ride_view);
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        //final int index =holder.getBindingAdapterPosition();
        String scheduleRideFrom = scheduleRideList.get(position).get("From").toString();
        String scheduleRideTo = scheduleRideList.get(position).get("To").toString();
        String scheduleRideDate = scheduleRideList.get(position).get("Date").toString();
        String scheduleRideTime = scheduleRideList.get(position).get("Time").toString();
        String scheduleRideCharge = scheduleRideList.get(position).get("Charge").toString();
        String scheduleRideNoOfPeople = scheduleRideList.get(position).get("NoOfPeople").toString();
        String scheduleRideWeight = scheduleRideList.get(position).get("Weight").toString();
        String scheduleRideUserId = scheduleRideList.get(position).get("UserId").toString();

        int length = scheduleRideFrom.length();
        if(length > 15)
            scheduleRideFrom = scheduleRideFrom.substring(0,12) + "...";

        length = scheduleRideTo.length();
        if(length > 15)
            scheduleRideTo = scheduleRideTo.substring(0,12) + "...";


        if(Objects.requireNonNull(scheduleRideList.get(position).get("Status")).toString().equals("Start")){
            RideStartViewHolder rideStartViewHolder = (RideStartViewHolder) holder;
            rideStartViewHolder.ride_start_name.setText(scheduleRideFrom + "->" + scheduleRideTo);
            rideStartViewHolder.ride_start_time.setText(scheduleRideDate + " " + scheduleRideTime);
            rideStartViewHolder.ride_start_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fStore = FirebaseFirestore.getInstance();
                    UserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    DocumentReference documentReference = fStore.collection("scheduleRide").document(UserId+"Ride");
                    Map<String, Object> scheduleRide = new HashMap<>();
                    scheduleRide.put("From", scheduleRideList.get(position).get("From").toString());
                    scheduleRide.put("To", scheduleRideList.get(position).get("To").toString());
                    scheduleRide.put("Date", scheduleRideDate);
                    scheduleRide.put("Time", scheduleRideTime);
                    scheduleRide.put("Charge", scheduleRideCharge);
                    scheduleRide.put("NoOfPeople", scheduleRideNoOfPeople);
                    scheduleRide.put("Weight", scheduleRideWeight);
                    scheduleRide.put("Status", "CarMode");
                    scheduleRide.put("UserId", scheduleRideUserId);
                    documentReference.set(scheduleRide).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Ride is in CarMode", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    });
                }
            });
        }
        else if(Objects.requireNonNull(scheduleRideList.get(position).get("Status")).toString().equals("CarMode")){
            RideCarModeViewHolder rideCarModeViewHolder = (RideCarModeViewHolder) holder;
            rideCarModeViewHolder.ride_car_mode_name.setText(scheduleRideFrom + "->" + scheduleRideTo);
            rideCarModeViewHolder.ride_car_mode_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fStore = FirebaseFirestore.getInstance();
                    UserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    DocumentReference documentReference = fStore.collection("scheduleRide").document(UserId+"Ride");
                    Map<String, Object> scheduleRide = new HashMap<>();
                    scheduleRide.put("From", scheduleRideList.get(position).get("From").toString());
                    scheduleRide.put("To", scheduleRideList.get(position).get("To").toString());
                    scheduleRide.put("Date", scheduleRideDate);
                    scheduleRide.put("Time", scheduleRideTime);
                    scheduleRide.put("Charge", scheduleRideCharge);
                    scheduleRide.put("NoOfPeople", scheduleRideNoOfPeople);
                    scheduleRide.put("Weight", scheduleRideWeight);
                    scheduleRide.put("Status", "Reached");
                    scheduleRide.put("UserId", scheduleRideUserId);
                    documentReference.set(scheduleRide).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Ride is finish", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    });
                }
            });
        }
        else if(Objects.requireNonNull(scheduleRideList.get(position).get("Status")).toString().equals("Reached")) {
            RideReachedViewHolder rideReachedViewHolder = (RideReachedViewHolder) holder;
            rideReachedViewHolder.ride_reached_name.setText(scheduleRideFrom + "->" + scheduleRideTo);
            fStore = FirebaseFirestore.getInstance();
            UserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            DocumentReference documentReference = fStore.collection("users").document(UserId);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                    assert value != null;
                    String name = Objects.requireNonNull(value.get("Name")).toString();
                    rideReachedViewHolder.ride_reached_pick_name.setText(name + " and " + scheduleRideNoOfPeople + " others");
                }
            });
            rideReachedViewHolder.ride_reached_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Ride is already finished", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return scheduleRideList.size();
    }

    public class RideStartViewHolder extends RecyclerView.ViewHolder {

        TextView ride_start_name, ride_start_time, ride_start_moreInfo, ride_start_requests;
        Button ride_start_btn;
        View view;

        public RideStartViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            ride_start_name = itemView.findViewById(R.id.ride_start_name);
            ride_start_time = itemView.findViewById(R.id.ride_start_time);
            ride_start_moreInfo = itemView.findViewById(R.id.ride_start_moreInfo);
            ride_start_requests = itemView.findViewById(R.id.ride_start_requests);
            ride_start_btn = itemView.findViewById(R.id.ride_start_btn);
            view = itemView;
        }
    }

    public class RideCarModeViewHolder extends RecyclerView.ViewHolder {

        TextView ride_car_mode_name;
        Button ride_car_mode_btn;
        View view;

        public RideCarModeViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            ride_car_mode_name = itemView.findViewById(R.id.ride_car_mode_name);
            ride_car_mode_btn = itemView.findViewById(R.id.ride_car_mode_btn);
            view = itemView;
        }
    }

    public class RideReachedViewHolder extends RecyclerView.ViewHolder {

        TextView ride_reached_name, ride_reached_pick_name;
        Button ride_reached_btn;
        View view;

        public RideReachedViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            ride_reached_name = itemView.findViewById(R.id.ride_reached_name);
            ride_reached_pick_name = itemView.findViewById(R.id.ride_reached_pick_name);
            ride_reached_btn = itemView.findViewById(R.id.ride_reached_btn);
            view = itemView;
        }
    }

    public class CarPoolPendingViewHolder extends RecyclerView.ViewHolder {

        TextView carpool_pending_name, carpool_pending_time, carpool_pending_people;
        Button carpool_pending_btn;
        View view;

        public CarPoolPendingViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            carpool_pending_name = itemView.findViewById(R.id.carpool_pending_name);
            carpool_pending_time = itemView.findViewById(R.id.carpool_pending_time);
            carpool_pending_people = itemView.findViewById(R.id.carpool_pending_people);
            carpool_pending_btn = itemView.findViewById(R.id.carpool_pending_btn);
            view = itemView;
        }
    }

    public class CarPoolWaitingViewHolder extends RecyclerView.ViewHolder {

        TextView ride_waiting_name, ride_waiting_pick_name;
        Button ride_waiting_btn;
        View view;

        public CarPoolWaitingViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            ride_waiting_name = itemView.findViewById(R.id.ride_waiting_name);
            ride_waiting_pick_name = itemView.findViewById(R.id.ride_waiting_pick_name);
            ride_waiting_btn = itemView.findViewById(R.id.ride_waiting_btn);
            view = itemView;
        }
    }

    public class CarPoolOnwayViewHolder extends RecyclerView.ViewHolder {

        TextView carpool_onway_name, carpool_onway_pick_name;
        Button carpool_onway_btn;
        View view;

        public CarPoolOnwayViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            carpool_onway_name = itemView.findViewById(R.id.carpool_onway_name);
            carpool_onway_pick_name = itemView.findViewById(R.id.carpool_onway_pick_name);
            carpool_onway_btn = itemView.findViewById(R.id.carpool_onway_btn);
            view = itemView;
        }
    }

    public class CarPoolReachedViewHolder extends RecyclerView.ViewHolder {

        TextView carpool_reached_name, carpool_reached_pick_name;
        Button carpool_reached_btn;
        View view;

        public CarPoolReachedViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            carpool_reached_name = itemView.findViewById(R.id.carpool_reached_name);
            carpool_reached_pick_name = itemView.findViewById(R.id.carpool_reached_pick_name);
            carpool_reached_btn = itemView.findViewById(R.id.carpool_reached_btn);
            view = itemView;
        }
    }

}
