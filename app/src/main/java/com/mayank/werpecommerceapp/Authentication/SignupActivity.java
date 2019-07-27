package com.mayank.werpecommerceapp.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mayank.werpecommerceapp.Activity.HomeActivity;
import com.mayank.werpecommerceapp.R;

import java.util.HashMap;

import javax.xml.validation.Validator;
/**
 * Created by MAYANK SINGH on 15-07-2019.
 */
public class SignupActivity extends AppCompatActivity  implements View.OnClickListener {

    private EditText inputname,inputemail,inputpasswword;
    private TextView tvLogin;
    Button reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tvLogin = findViewById(R.id.tvlogin);
        inputname = findViewById(R.id.signupname);
        inputemail = findViewById(R.id.signupPhone);
        inputpasswword = findViewById(R.id.signuppassword);
         reg = findViewById(R.id.signupBtn);

        reg.setOnClickListener(this);
        tvLogin.setOnClickListener(this);



    }

    public void Signup(View view) {
            createAccount();
    }

    private void createAccount() {

        String name  =  inputname.getText().toString();
        String email = inputemail.getText().toString();
        String password = inputpasswword.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(getApplicationContext(),"Please Enter the Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(),"Please Enter the Email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(),"Please Enter the password",Toast.LENGTH_SHORT).show();
        }
        else{
            ValidatephoneNumber(name, email,password);
        }
    }

    private void ValidatephoneNumber(final String name, final String email, final String password) {

    final DatabaseReference RootRef;
    RootRef = FirebaseDatabase.getInstance().getReference();

    RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(!dataSnapshot.child("Users").child(email).exists()){

                HashMap<String,Object> userdatamap = new HashMap<>();
                userdatamap.put("phone",email);
                userdatamap.put("password",password);
                userdatamap.put("name",name);

                RootRef.child("Users").child(email).updateChildren(userdatamap).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Account Created Successfully!",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
            else {
                Toast.makeText(getApplicationContext(),"User already exists",Toast.LENGTH_SHORT).show();

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
            case R.id.signupBtn:
                register();
                break;
            case R.id.tvlogin:
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();
                break;
            default:

        }
    }

    private void register(){
        String email = inputemail.getText().toString();
        String password = inputpasswword.getText().toString();
        if(email.isEmpty() && password.isEmpty()){
            displayToast("Username/password field empty");
        }else{
            displayToast("User registered");
            finish();
        }
    }

    private void displayToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
