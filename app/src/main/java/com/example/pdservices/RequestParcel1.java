package com.example.pdservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

public class RequestParcel1 extends AppCompatActivity {

    EditText parcel_from, parcel_to, parcel_date, parcel_time, parcel_weight, parcel_note;
    Button parcel_cancel, parcel_next;
    boolean validateForm = false;
    HashMap<String, Object> parcelMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_parcel1);

        parcel_cancel = findViewById(R.id.parcel_cancel);
        parcel_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        parcel_from = findViewById(R.id.parcel_from);
        parcel_to = findViewById(R.id.parcel_to);
        parcel_date = findViewById(R.id.parcel_date);
        parcel_time = findViewById(R.id.parcel_time);
        parcel_weight = findViewById(R.id.parcel_weight);
        parcel_note = findViewById(R.id.parcel_note);
        parcel_next = findViewById(R.id.parcel_next);

        parcel_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm = validateFields();
                if(validateForm) {
                    String parcelFrom = parcel_from.getText().toString().trim();
                    String parcelTo = parcel_to.getText().toString().trim();
                    String parcelDate = parcel_date.getText().toString().trim();
                    String parcelTime = parcel_time.getText().toString().trim();
                    String parcelWeight = parcel_weight.getText().toString().trim();
                    String parcelNote = parcel_note.getText().toString().trim();

                    parcelMap = new HashMap<>();
                    parcelMap.put("From", parcelFrom);
                    parcelMap.put("To", parcelTo);
                    parcelMap.put("Date", parcelDate);
                    parcelMap.put("Time", parcelTime);
                    parcelMap.put("Weight", parcelWeight + " kg");
                    parcelMap.put("Note", parcelNote);

                    Intent intent = new Intent(RequestParcel1.this, RequestParcel2.class);
                    intent.putExtra("parcelMap", parcelMap);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validateFields() {
        if(parcel_from.length() == 0) {
            parcel_from.setError("Add Location");
            return false;
        }
        if(parcel_to.length() == 0) {
            parcel_to.setError("Add Location");
            return false;
        }
        if(parcel_date.length() == 0) {
            parcel_date.setError("Add Date");
            return false;
        }
        if(parcel_time.length() == 0) {
            parcel_time.setError("Add Time");
            return false;
        }
        if(parcel_weight.length() == 0) {
            parcel_weight.setError("Add No. Of People");
            return false;
        }
        return true;
    }
}