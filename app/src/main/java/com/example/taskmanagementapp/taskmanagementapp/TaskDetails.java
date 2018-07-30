package com.example.taskmanagementapp.taskmanagementapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TaskDetails extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewTitle;
    private  TextView textViewDate;
    private  TextView textViewDescription;
    private ImageView imageView;
    private Button buttonBack;
    private Bundle extras;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        setUpDetails();
        if(!extras.isEmpty()){
            textViewTitle.setText(extras.getString("Title"));
            textViewDate.setText(extras.getString("Date"));
            textViewDescription.setText(extras.getString("Description"));
            String uri = extras.getString("Image");
            Picasso.with(this).load(uri).into(imageView);
        }
        buttonBack.setOnClickListener(this);
    }

    public void setUpDetails(){
        extras = getIntent().getExtras();
        textViewTitle = (TextView) findViewById(R.id.textViewDetTitle);
        textViewDate = (TextView) findViewById(R.id.textViewDetDate);
        textViewDescription = (TextView) findViewById(R.id.textViewDetDesc);
        imageView = (ImageView) findViewById(R.id.imageViewDetImg);
        buttonBack = (Button) findViewById(R.id.buttonDetBack);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonDetBack:
                startActivity(new Intent(TaskDetails.this,Home.class));
                finish();
                break;
        }
    }
}
