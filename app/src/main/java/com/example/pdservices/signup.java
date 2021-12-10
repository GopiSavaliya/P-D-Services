package com.example.pdservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    EditText name, email, number, password, confirmPassword, address;

    Button btnSignUp;

    TextView signIn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name = findViewById(R.id.Name);
        email = findViewById(R.id.email);
        number = findViewById(R.id.mobile_number);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        address = findViewById(R.id.address);

        btnSignUp = findViewById(R.id.sign_up_button);
        signIn = findViewById(R.id.sign_in);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();
                String Email = email.getText().toString();
                String Number = number.getText().toString();
                String Password = password.getText().toString();
                String cPassword = confirmPassword.getText().toString();
                String Address = address.getText().toString();

                if(Name.equals("") || Email.equals("") || Number.equals("") || Password.equals("") || cPassword.equals("") || Address.equals("")){
                    Toast.makeText(signup.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }else{
                    if(Password.equals(cPassword)){
                        if(Password.length() < 6){
                            Toast.makeText(signup.this, "Password is too short!", Toast.LENGTH_SHORT).show();
                        }else{
                            mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        userId = mAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference = fStore.collection("users").document(userId);

                                        Map<String, Object> user = new HashMap<>();
                                        user.put("Name", Name);
                                        user.put("Email", Email);
                                        user.put("Phone", Number);
                                        user.put("Address", Address);

                                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("TAG:","Success! "+userId);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("TAG:","Failure: "+e.toString());

                                            }
                                        });

                                        Toast.makeText(signup.this, "Signed Up Successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(signup.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });
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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
}