package com.example.taskmanagementapp.taskmanagementapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddTask extends AppCompatActivity implements View.OnClickListener{

    private Button buttonSave;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private ImageButton imageButtonA;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;
    private FirebaseUser user;
    private Uri taskImage;
    private static final int GALLERY_CODE = 1;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        setUp();
        imageButtonA.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

    public void setUp(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        buttonSave = (Button) findViewById(R.id.buttonASave);
        editTextTitle=(EditText) findViewById(R.id.editTextATask);
        editTextDescription=(EditText) findViewById(R.id.editTextADescription);
        imageButtonA = (ImageButton) findViewById(R.id.imageButtonA);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageButtonA:
                imageRetrieve();
                break;
            case R.id.buttonASave:
                createNewTask();
                break;
        }
    }

    public void imageRetrieve(){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_CODE);
    }

    public void createNewTask(){
        final String title = editTextTitle.getText().toString().trim();
        final String description = editTextDescription.getText().toString().trim();
        progressDialog.setMessage("Creating task...");
        progressDialog.show();
        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && taskImage!=null){
            StorageReference filepath = storageReference.child("User_task_images").child(taskImage.getLastPathSegment());
            filepath.putFile(taskImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c);
                    Uri downlaodUrl = taskSnapshot.getDownloadUrl();
                    String key = mDatabase.child("Tasks").push().getKey();
                    String temp = user.getUid().toString()+key;
                    Map<String, String> dataToSave = new HashMap<>();
                    dataToSave.put("title",title);
                    dataToSave.put("description",description);
                    dataToSave.put("image",downlaodUrl.toString());
                    dataToSave.put("dateTaskAdded",formattedDate);
                    dataToSave.put("taskID",user.getUid().toString());
                    dataToSave.put("removeID",user.getUid()+key);
                    mDatabase.child("Tasks").child(user.getUid()+key).setValue(dataToSave);
                    progressDialog.dismiss();
                    startActivity(new Intent(AddTask.this,Home.class));
                    finish();
                }

            });
        }else {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GALLERY_CODE && resultCode  == RESULT_OK){
            taskImage = data.getData();
            imageButtonA.setImageURI(taskImage);
        }
    }
}
