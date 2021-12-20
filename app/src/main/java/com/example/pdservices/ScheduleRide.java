package com.example.pdservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ScheduleRide extends AppCompatActivity {

    Button schedule_ride_cancel, schedule_ride_schedule;
    EditText schedule_ride_from, schedule_ride_to, schedule_ride_date, schedule_ride_time;
    EditText schedule_ride_charge, schedule_ride_no_of_people, schedule_ride_weight;
    boolean validateForm = false;
    private FirebaseFirestore fStore;
    private String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_ride);

        schedule_ride_cancel = findViewById(R.id.schedule_ride_cancel);
        schedule_ride_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        schedule_ride_from = findViewById(R.id.schedule_ride_from);
        schedule_ride_to = findViewById(R.id.schedule_ride_to);
        schedule_ride_date = findViewById(R.id.schedule_ride_date);
        schedule_ride_time = findViewById(R.id.schedule_ride_time);
        schedule_ride_charge = findViewById(R.id.schedule_ride_charge);
        schedule_ride_no_of_people = findViewById(R.id.schedule_ride_no_of_people);
        schedule_ride_weight = findViewById(R.id.schedule_ride_weight);
        schedule_ride_schedule = findViewById(R.id.schedule_ride_schedule);

        schedule_ride_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm = validateFields();

                if(validateForm) {
                    String scheduleRideFrom = schedule_ride_from.getText().toString().trim();
                    String scheduleRideTo = schedule_ride_to.getText().toString().trim();
                    String scheduleRideDate = schedule_ride_date.getText().toString().trim();
                    String scheduleRideTime = schedule_ride_time.getText().toString().trim();
                    String scheduleRideCharge = schedule_ride_charge.getText().toString().trim();
                    String scheduleRideNoOfPeople = schedule_ride_no_of_people.getText().toString().trim();
                    String scheduleRideWeight = schedule_ride_weight.getText().toString().trim();
                    String scheduleRideUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    //instantiate firebase firestore
                    fStore = FirebaseFirestore.getInstance();
                    UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    //create document reference(collection)
                    DocumentReference documentReference = fStore.collection("scheduleRide").document(UserId+"Ride");
                    Map<String, Object> scheduleRide = new HashMap<>();
                    scheduleRide.put("From", scheduleRideFrom);
                    scheduleRide.put("To", scheduleRideTo);
                    scheduleRide.put("Date", scheduleRideDate);
                    scheduleRide.put("Time", scheduleRideTime);
                    scheduleRide.put("Charge", scheduleRideCharge);
                    scheduleRide.put("NoOfPeople", scheduleRideNoOfPeople);
                    scheduleRide.put("Weight", scheduleRideWeight);
                    scheduleRide.put("Status", "Start");
                    scheduleRide.put("UserId", scheduleRideUserId);

                    //add Map to the collection
                    documentReference.set(scheduleRide).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ScheduleRide.this, "Ride is scheduled", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ScheduleRide.this, MainActivity.class));
                        }
                    });
                }
            }

            private boolean validateFields() {
                if(schedule_ride_from.length() == 0) {
                    schedule_ride_from.setError("Add Location");
                    return false;
                }
                if(schedule_ride_to.length() == 0) {
                    schedule_ride_to.setError("Add Location");
                    return false;
                }
                if(schedule_ride_date.length() == 0) {
                    schedule_ride_date.setError("Add Date");
                    return false;
                }
                if(schedule_ride_time.length() == 0) {
                    schedule_ride_time.setError("Add Time");
                    return false;
                }
                if(schedule_ride_charge.length() == 0) {
                    schedule_ride_charge.setError("Add Charge");
                    return false;
                }
                if(schedule_ride_no_of_people.length() == 0) {
                    schedule_ride_no_of_people.setError("Add No. Of People");
                    return false;
                }
                if(schedule_ride_weight.length() == 0) {
                    schedule_ride_weight.setError("Add Weight");
                    return false;
                }
                return true;
            }
        });
    }
}