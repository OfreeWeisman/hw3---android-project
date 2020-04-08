package com.technion.android.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button login;
    Button create_account;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email = findViewById(R.id.log_email);
        password = findViewById(R.id.log_password);
        login = findViewById(R.id.login);
        create_account = findViewById(R.id.create_account);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_account.setEnabled(false);
                login.setEnabled(false);
                loginUser();

            }
        });

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignup();
            }
        });



    }

    public void openSignup(){
        Intent intent = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    public void loginUser(){

        String user_email = email.getText().toString();
        String user_password = password.getText().toString();

        if(user_email.isEmpty()){
            Toast.makeText(MainActivity.this,"Please enter your email", Toast.LENGTH_LONG).show();
        } else {
            if(user_password.isEmpty()){
                Toast.makeText(MainActivity.this,"Please enter your password", Toast.LENGTH_LONG).show();
            } else {
                mAuth.signInWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //for me:
                            Toast.makeText(MainActivity.this,"user is signed in", Toast.LENGTH_LONG).show();
                            //
                            Log.d(this.getClass().getName(),"SUCCESS - user is logged in");
                            create_account.setEnabled(true);
                            login.setEnabled(true);
                            //TODO : go to list activity!

                            //
                    /*
                    Item item = new Item("bye");
                    DocumentReference item_ref = db.collection("users").document(mAuth.getCurrentUser().getEmail()).collection("items").document();
                    item_ref.set(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"SUCCESS - item added", Toast.LENGTH_LONG).show();
                            }
                        }
                    });*/
                            //

                        }
                        else{
                            Toast.makeText(MainActivity.this,"Failed to log in. Try again", Toast.LENGTH_LONG).show();
                            Log.w(this.getClass().getName(),"FAILURE - log in failed");

                            create_account.setEnabled(true);
                            login.setEnabled(true);
                        }


                    }
                });
            }
        }
        
    }


}
