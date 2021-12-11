package com.example.pdservices;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class request_pool_parcel1 extends AppCompatActivity {

    private LinearLayout margin;
    private TextView select;
    private DatePickerDialog.OnDateSetListener mDatesetListner;


    public request_pool_parcel1(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        margin = findViewById(R.id.margin);
        select = findViewById(R.id.select);
        setContentView(R.layout.activity_request_pool_parcel1);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        request_pool_parcel1.this,

                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDatesetListner,
                        year,month,day
                );

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }
}