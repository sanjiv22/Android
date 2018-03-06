package com.example.sanjiv.workmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnSignin, btnSignup;
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabaseUsers;
    private ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword =(EditText) findViewById(R.id.etPassword);
        btnSignin = (Button) findViewById(R.id.btnSignin);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        mAuth = FirebaseAuth.getInstance();
        mprogress =new ProgressDialog(this);
        mdatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabaseUsers.keepSynced(true);
        btnSignin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                checkLogin();
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerintent = new Intent(LoginActivity.this,RegisterActivity.class);
                registerintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(registerintent);
            }
        });
    }
    private void checkLogin() {
        String EmailValue = etEmail.getText().toString().trim();
        String PasswordValue = etPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(EmailValue) && !TextUtils.isEmpty(PasswordValue))
        {
            mprogress.setMessage("Checking Login....");
            mprogress.show();
            mAuth.signInWithEmailAndPassword(EmailValue,PasswordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mprogress.dismiss();
                        CheckUserExist();
                    }
                    else{
                        mprogress.dismiss();
                        Toast.makeText(LoginActivity.this,"Incorrect Email/Password",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    private void CheckUserExist() {
        final String user_id= mAuth.getCurrentUser().getUid();
        mdatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(user_id))
                {
                    Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(mainIntent);
                }
                else
                {
                    Intent setupIntent = new Intent(LoginActivity.this,SetupActivity.class);
                    startActivity(setupIntent);


                    Toast.makeText(LoginActivity.this,"You need to set up your Account",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
