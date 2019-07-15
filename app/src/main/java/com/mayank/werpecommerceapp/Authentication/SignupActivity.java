package com.mayank.werpecommerceapp.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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

public class SignupActivity extends AppCompatActivity {

    private EditText inputname,inputphone,inputpasswword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        inputname = findViewById(R.id.name);
        inputphone = findViewById(R.id.phone);
        inputpasswword = findViewById(R.id.password);

    }

    public void Signup(View view) {
            createAccount();
    }

    private void createAccount() {

        String name  =  inputname.getText().toString();
        String phone = inputphone.getText().toString();
        String password = inputpasswword.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(getApplicationContext(),"Please Enter the Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(getApplicationContext(),"Please Enter the Email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(),"Please Enter the password",Toast.LENGTH_SHORT).show();
        }
        else{
            ValidatephoneNumber(name, phone,password);
        }
    }

    private void ValidatephoneNumber(final String name, final String phone, final String password) {

    final DatabaseReference RootRef;
    RootRef = FirebaseDatabase.getInstance().getReference();

    RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(!dataSnapshot.child("Users").child(phone).exists()){

                HashMap<String,Object> userdatamap = new HashMap<>();
                userdatamap.put("phone",phone);
                userdatamap.put("password",password);
                userdatamap.put("name",name);

                RootRef.child("Users").child(phone).updateChildren(userdatamap).
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
}
