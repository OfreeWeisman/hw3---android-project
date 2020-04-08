package com.technion.android.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button signup;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup.setEnabled(false);
                onSignIn();
            }
        });
    }


    public void onSignIn() {
        final String user_email = email.getText().toString();
        String user_password = password.getText().toString();

        if(user_email.isEmpty()){
            Toast.makeText(SignupActivity.this,"Please enter your email", Toast.LENGTH_LONG).show();
        } else {
            if(user_password.isEmpty()){
                Toast.makeText(SignupActivity.this,"Please enter your password", Toast.LENGTH_LONG).show();
            } else {
                mAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //for me:
                            //Toast.makeText(SignupActivity.this,"SUCCESS - user is signed up", Toast.LENGTH_LONG).show();
                            //
                            Log.d(this.getClass().getName(), "SUCCESS - user is signed in");

                            Map<String, Object> user = new HashMap<>();
                            user.put("email", mAuth.getCurrentUser().getEmail());

                            CollectionReference col_ref = db.collection("users");

                            col_ref.document(user_email).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //for me:
                                    //Toast.makeText(SignupActivity.this,"SUCCESS - user added to db", Toast.LENGTH_LONG).show();
                                    //
                                    Log.d(this.getClass().getName(), "SUCCESS - new user added to database");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //for me:
                                    //Toast.makeText(SignupActivity.this,"FAILURE - can't add user to db", Toast.LENGTH_LONG).show();
                                    //

                                    Log.w(this.getClass().getName(), "FAILURE - failed to add new user to the database");
                                }
                            });

                            finish();
                            signup.setEnabled(true);


                        } else{
                            Toast.makeText(SignupActivity.this,"Sign up failed. Please try again", Toast.LENGTH_LONG).show();
                            Log.w(this.getClass().getName(), "FAILURE - signup failed");
                            signup.setEnabled(true);

                        }
                    }
                });
            }
        }


    }
}
