package com.example.pdservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

public class RequestCarPool1 extends AppCompatActivity {

    EditText car_pool_from, car_pool_to, car_pool_date, car_pool_time, car_pool_NoOfPeople, car_pool_Note;
    Button car_pool_cancel, car_pool_next;
    boolean validateForm = false;
    HashMap<String, Object> carPoolMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_carpool1);

        car_pool_cancel = findViewById(R.id.car_pool_cancel);
        car_pool_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        car_pool_from = findViewById(R.id.car_pool_from);
        car_pool_to = findViewById(R.id.car_pool_to);
        car_pool_date = findViewById(R.id.car_pool_date);
        car_pool_time = findViewById(R.id.car_pool_time);
        car_pool_NoOfPeople = findViewById(R.id.car_pool_NoOfPeople);
        car_pool_Note = findViewById(R.id.car_pool_Note);
        car_pool_next = findViewById(R.id.car_pool_next);

        car_pool_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm = validateFields();
                if(validateForm) {
                    String carPoolFrom = car_pool_from.getText().toString().trim();
                    String carPoolTo = car_pool_to.getText().toString().trim();
                    String carPoolDate = car_pool_date.getText().toString().trim();
                    String carPoolTime = car_pool_time.getText().toString().trim();
                    String carPoolNoOfPeople = car_pool_NoOfPeople.getText().toString().trim();
                    String carPoolNote = car_pool_Note.getText().toString().trim();

                    carPoolMap = new HashMap<>();
                    carPoolMap.put("From", carPoolFrom);
                    carPoolMap.put("To", carPoolTo);
                    carPoolMap.put("Date", carPoolDate);
                    carPoolMap.put("Time", carPoolTime);
                    carPoolMap.put("NoOfPeople", carPoolNoOfPeople);
                    carPoolMap.put("Note", carPoolNote);

                    Intent intent = new Intent(RequestCarPool1.this, RequestCarPool2.class);
                    intent.putExtra("carPoolMap", carPoolMap);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validateFields() {
        if(car_pool_from.length() == 0) {
            car_pool_from.setError("Add Location");
            return false;
        }
        if(car_pool_to.length() == 0) {
            car_pool_to.setError("Add Location");
            return false;
        }
        if(car_pool_date.length() == 0) {
            car_pool_date.setError("Add Date");
            return false;
        }
        if(car_pool_time.length() == 0) {
            car_pool_time.setError("Add Time");
            return false;
        }
        if(car_pool_NoOfPeople.length() == 0) {
            car_pool_NoOfPeople.setError("Add No. Of People");
            return false;
        }
        return true;
    }
}