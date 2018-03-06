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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPassword;
    private Button btnSignup;
    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mprogress;
    private DatabaseReference mdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSignup = (Button)findViewById(R.id.btnSignup);
        mprogress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class ));

                }
            }
        };
        btnSignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
    }


    private void startRegister() {
        final String NameValue = etName.getText().toString().trim();
        String EmailValue = etEmail.getText().toString().trim();
        String PasswordValue = etPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(NameValue) && !TextUtils.isEmpty(EmailValue) && !TextUtils.isEmpty(PasswordValue) )
        {
            mprogress.setMessage("Signing Up.....");
            mprogress.show();
            mAuth.createUserWithEmailAndPassword(EmailValue,PasswordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mdatabase.child(user_id);
                        current_user_db.child("Name").setValue(NameValue);
                        current_user_db.child("Image").setValue("default");
                        mprogress.dismiss();
                       // Toast.makeText(RegisterActivity.this,"Registered", Toast.LENGTH_LONG).show();
                        Intent mainIntent = new Intent(RegisterActivity.this,SetupActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }

                }
            });

        }
        else {

            mprogress.setMessage("Email/Password Required!!");
            mprogress.show();
            // Toast.makeText(LoginActivity.this,"Email/Password Required!!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected  void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
