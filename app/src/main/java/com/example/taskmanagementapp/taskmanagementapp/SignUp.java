package com.example.taskmanagementapp.taskmanagementapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.taskmanagementapp.taskmanagementapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements View.OnClickListener{


    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private EditText editTextName;
    private EditText editTextLastName;
    private  EditText editTextEmail;
    private EditText editTextPass;
    private EditText editTextID;
    private ImageButton imageButtonProfile;
    private Button buttonSignUp;
    private Uri profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUp();
        buttonSignUp.setOnClickListener(this);
        imageButtonProfile.setOnClickListener(this);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };
    }


    public void setUp(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        user = mAuth.getCurrentUser();
        imageButtonProfile = this.findViewById(R.id.imageButtonProfile);
        editTextName = this.findViewById(R.id.editTextSName);
        editTextLastName = this.findViewById(R.id.editTextSLastName);
        editTextEmail = this.findViewById(R.id.editTextSEmail);
        editTextPass = this.findViewById(R.id.editTextSPass);
        editTextID = this.findViewById(R.id.editTextSID);
        buttonSignUp = this.findViewById(R.id.buttonSSignUp);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSSignUp:
                createNewUser();
                break;
            case R.id.imageButtonProfile:
                         CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle("My Crop")
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setCropMenuCropButtonTitle("Done")
                        .setRequestedSize(400, 400)
                        .setCropMenuCropButtonIcon(R.drawable.donewhite).start(this);
                break;
        }
    }

    private void createNewUser() {
        final String name = editTextName.getText().toString().trim();
        final String lastName = editTextLastName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String pass = editTextPass.getText().toString().trim();
        final String id = editTextID.getText().toString().trim();
        //final String userID=name+" "+lastName+" "+id;
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(id) && profileImage != null){
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                StorageReference filepath = storageReference.child("User_profile_images").child(profileImage.getLastPathSegment());
                                filepath.putFile(profileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        Uri downlaodUrl = taskSnapshot.getDownloadUrl();
                                        DatabaseReference newPost = mDatabase.push();
                                        Map<String, String> dataToSave = new HashMap<>();
                                        dataToSave.put("name",name);
                                        dataToSave.put("lastName",lastName);
                                        dataToSave.put("email",email);
                                        dataToSave.put("image",downlaodUrl.toString());
                                        dataToSave.put("id",id);
                                        progressDialog.dismiss();
                                        startActivity(new Intent(SignUp.this,Home.class));
                                        finish();
                                        String userId = user.getUid();
                                        mDatabase.child("Users").child(userId).setValue(dataToSave);

                                    }
                                });
                            } else {
                                Toast.makeText(SignUp.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
    }else {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImage = result.getUri();
                imageButtonProfile.setImageURI(profileImage);
                Toast.makeText(
                        this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG)
                        .show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
