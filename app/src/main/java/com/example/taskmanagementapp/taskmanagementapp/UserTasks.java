package com.example.taskmanagementapp.taskmanagementapp;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.taskmanagementapp.taskmanagementapp.Data.TaskRecyclerAdapter;
import com.example.taskmanagementapp.taskmanagementapp.Data.UserTasksAdapter;
import com.example.taskmanagementapp.taskmanagementapp.Model.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserTasks extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Bundle extras;
    private List<Task> taskList;
    private String key;
    private DatabaseReference databaseRef;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tasks);
        userTasksSetUp();
        if(!extras.isEmpty()){
            key = extras.getString("Key");
        }
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };
        user = auth.getCurrentUser();
    }

    public void userTasksSetUp(){
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference().child("Tasks");
        databaseRef.keepSynced(true);
        auth = FirebaseAuth.getInstance();
        taskList = new ArrayList<>();
        extras = getIntent().getExtras();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTasks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Task task = dataSnapshot.getValue(Task.class);
                if(task.getTaskID().equals(key))
                     taskList.add(task);
                adapter = new UserTasksAdapter(UserTasks.this,taskList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
