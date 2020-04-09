package com.technion.android.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListActivity extends AppCompatActivity {

    private Button logout;
    private Button insert;
    private EditText text;
    private MyRecyclerAdapter adapter;
    //private RecyclerView.LayoutManager layout_manager;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mAuth = FirebaseAuth.getInstance();
       // db = FirebaseFirestore.getInstance();

        checkCurrentUser();



        insert = findViewById(R.id.insert);
        logout = findViewById(R.id.logout);
        text = findViewById(R.id.list_input);



        //check if user is logged in
        getUserItems();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.setEnabled(false);
                insert.setEnabled(false);
                if(mAuth.getCurrentUser() != null){
                    mAuth.signOut();
                }
                openSignIn();
            }
        });
        insert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItem();
                }
            });

    }



    private void getUserItems() {
        String current_email = mAuth.getCurrentUser().getEmail();
        CollectionReference items_ref = db.collection("users").document(current_email).collection("items");
        Query query = items_ref.orderBy("input", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>().setQuery(query, Item.class).build();

        adapter = new MyRecyclerAdapter(options);
        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        LinearLayoutManager layout_manager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layout_manager);
        recycler.setAdapter(adapter);
    }

    public void checkCurrentUser(){
        if (mAuth.getCurrentUser().getEmail() == null){
            openSignIn();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkCurrentUser();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void addItem(){
        insert.setEnabled(false);
        logout.setEnabled(false);
        String input = text.getText().toString();
        if(input.isEmpty()){
            Toast.makeText(ListActivity.this, "Nothing to insert", Toast.LENGTH_LONG).show();
        } else {
            Item item = new Item(input);
            DocumentReference item_ref = db.collection("users").document(mAuth.getCurrentUser().getEmail()).collection("items").document();
            item_ref.set(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        //for me:
                        Toast.makeText(ListActivity.this, "SUCCESS - item added", Toast.LENGTH_LONG).show();
                        //
                        Log.d(this.getClass().getName(), "SUCCESS - new item added");
                        text.getText().clear();

                    } else {
                        Toast.makeText(ListActivity.this, "FAILURE - can't add item. Please try again", Toast.LENGTH_LONG).show();
                        Log.w(this.getClass().getName(), "FAILURE - Failed to add item");
                    }
                }
            });
        }
        insert.setEnabled(true);
        logout.setEnabled(true);
    }

    public void openSignIn(){
        Intent intent = new Intent(ListActivity.this, MainActivity.class);
        //enable buttons
        logout.setEnabled(true);
        insert.setEnabled(true);

        finish();
        startActivity(intent);
    }
}
