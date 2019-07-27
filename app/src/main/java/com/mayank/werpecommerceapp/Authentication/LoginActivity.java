package com.mayank.werpecommerceapp.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mayank.werpecommerceapp.Activity.HomeActivity;
import com.mayank.werpecommerceapp.Activity.SplashActivity;
import com.mayank.werpecommerceapp.Models.Users;
import com.mayank.werpecommerceapp.R;
/**
 * Created by MAYANK SINGH on 15-07-2019.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText inputphone,inputpasswword;
    private String parentDBname = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputphone = findViewById(R.id.phone);
        inputpasswword = findViewById(R.id.password);
    }

    public void login(View view) {
        loginuser();
    }

    public void Signup(View view) {
        Intent intent=new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }
    private void loginuser() {
        String phone = inputphone.getText().toString();
        String password = inputpasswword.getText().toString();

        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(getApplicationContext(),"Please Enter the Email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(),"Please Enter the password",Toast.LENGTH_SHORT).show();
        }
        else{
            AllowAccestoAccount(phone,password);
        }
    }

    private void AllowAccestoAccount(final String phone, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(parentDBname).child(phone).exists()){
                    Users usersData = dataSnapshot.child(parentDBname).child(phone).getValue(Users.class);

                    assert usersData != null;
                    if(usersData.getPhone().equals(phone)){
                        if(usersData.getPassword().equals(password)){
                            Toast.makeText(getApplicationContext(),"User LoggedIn!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),"User not found!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
