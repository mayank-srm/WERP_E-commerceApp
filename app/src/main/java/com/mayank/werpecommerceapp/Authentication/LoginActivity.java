package com.mayank.werpecommerceapp.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.media.MediaCas;
import android.os.Bundle;
import android.service.textservice.SpellCheckerService;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    private EditText inputemail,inputpasswword;
    String user;
    String password;
    private String parentDBname = "Users";
    private Button login, register;
    private EditText etEmail, etPass;
    private DbHelper db;
    private com.mayank.werpecommerceapp.Authentication.Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DbHelper(this);
        session = new Session(this);
        inputemail = findViewById(R.id.loginEmail);
        inputpasswword = findViewById(R.id.password);
        login= (Button)findViewById(R.id.loginBtn);
         register = (Button)findViewById(R.id.signup);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

        if(session.loggedin()) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();

        }
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
        String email = inputemail.getText().toString();
        String password = inputpasswword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(),"Please Enter the Email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(),"Please Enter the password",Toast.LENGTH_SHORT).show();
        }
        else{
            AllowAccestoAccount(email,password);
        }
    }

    private void AllowAccestoAccount(final String email, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(parentDBname).child(email).exists()){
                    Users usersData = dataSnapshot.child(parentDBname).child(email).getValue(Users.class);

                    assert usersData != null;
                    if(usersData.getPhone().equals(email)){
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


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.loginBtn:
                Login();
                break;
            case R.id.signup:
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                break;
            default:

        }
    }

    private void Login(){
        String email = inputemail.getText().toString();
        String password = inputpasswword.getText().toString();

        if(db.getUser(email,password)){
            session.setLoggedin(true);
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Wrong email/password",Toast.LENGTH_SHORT).show();
        }
    }
}





