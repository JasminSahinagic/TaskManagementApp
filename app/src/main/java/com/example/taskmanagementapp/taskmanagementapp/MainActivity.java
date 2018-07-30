package com.example.taskmanagementapp.taskmanagementapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.support.v4.os.LocaleListCompat.create;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{



    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private  EditText editTextEmail;
    private EditText editTextPass;
    private Button buttonLogIn;
    private  Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Toast.makeText(MainActivity.this, "User connceted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "User not connceted", Toast.LENGTH_SHORT).show();
                }
            }
        };
        buttonLogIn.setOnClickListener(this);
        buttonSignUp.setOnClickListener(this);
    }

    public void setUp(){
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPass = (EditText) findViewById(R.id.editTextPass);
        buttonLogIn=(Button) findViewById(R.id.buttonLogIn);
        buttonSignUp=(Button) findViewById(R.id.buttonSignUp);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(authStateListener);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonLogIn:
                logIn();
                startActivity(new Intent(MainActivity.this,Home.class));
                finish();
                break;
            case R.id.buttonSignUp:
                startActivity(new Intent(MainActivity.this,SignUp.class));
                break;
        }

    }

    public void logIn(){
        String email = editTextEmail.getText().toString();
        String pass = editTextPass.getText().toString();
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)){
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "User is signed in.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}



