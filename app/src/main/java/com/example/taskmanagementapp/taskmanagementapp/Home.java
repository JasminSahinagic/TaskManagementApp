package com.example.taskmanagementapp.taskmanagementapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.taskmanagementapp.taskmanagementapp.Data.TaskRecyclerAdapter;
import com.example.taskmanagementapp.taskmanagementapp.Model.Task;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Home extends AppCompatActivity implements View.OnClickListener {


    private Button buttonAdd;
    private Button buttonTaskas;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private DatabaseReference databaseRef;
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Task> taskList;
    private List<Task> tempList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUp();
        buttonAdd.setOnClickListener(this);
        buttonTaskas.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonAddTask:
                //onCreateDialog();
                startActivity(new Intent(Home.this, AddTask.class));
                break;
            case R.id.buttonTasks:
                insertTaskKey(Home.this);
                break;
            case R.id.buttonHomeLogOut:
                mAuth.getInstance().signOut();
                startActivity(new Intent(Home.this, MainActivity.class));
                break;
        }
    }

    public void insertTaskKey(final Context ctx) {
        builder = new AlertDialog.Builder(this);
        inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.add_task_key, null);
        Button buttonKey = (Button) view.findViewById(R.id.buttonTasksSubmit);
        final EditText editTextKey = (EditText) view.findViewById(R.id.editTextTasksKey);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        buttonKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = editTextKey.getText().toString();
                Intent intent = new Intent(Home.this, UserTasks.class);
                intent.putExtra("Key", key);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Task task = dataSnapshot.getValue(Task.class);
                if (task.getTaskID().equals(user.getUid())) {
                    taskList.add(task);
                }
                ArrayList<Task> values = (ArrayList<Task>) taskList;
                HashSet<Task> uniqueValues = new HashSet<>(values);
                taskList.clear();
                for (Task value : uniqueValues) {
                    tempList.add(value);
                }
                adapter = new TaskRecyclerAdapter(Home.this, tempList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                taskList.clear();
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
    public void setUp() {
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference().child("Tasks");
        databaseRef.keepSynced(true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();
        tempList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        buttonAdd = (Button) findViewById(R.id.buttonAddTask);
        buttonTaskas = (Button) findViewById(R.id.buttonTasks);
    }
}



