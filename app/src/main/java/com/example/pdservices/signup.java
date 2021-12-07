package com.example.pdservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class signup extends AppCompatActivity {

    EditText name, email, number, password, confirmPassword;

    Button btnSignUp;

    TextView signIn;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name = findViewById(R.id.Name);
        email = findViewById(R.id.email);
        number = findViewById(R.id.mobile_number);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);

        btnSignUp = findViewById(R.id.sign_up_button);
        signIn = findViewById(R.id.sign_in);

        db = new DBHelper(this);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();
                String Email = email.getText().toString();
                String Number = number.getText().toString();
                String Password = password.getText().toString();
                String cPassword = confirmPassword.getText().toString();

                if(Name.equals("") || Email.equals("") || Number.equals("") || Password.equals("") || cPassword.equals("")){
                    Toast.makeText(signup.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }else{
                    if(Password.equals(cPassword)){
                        Boolean checkuser = db.checkEmail(Email);
                        if(!checkuser){
                            Boolean insert = db.insertData(Name, Email, Number, Password);
                            if(insert){
                                Toast.makeText(signup.this, "Signed Up Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(signup.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(signup.this, "User Already Exists!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(signup.this, "Password Not Matching!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}