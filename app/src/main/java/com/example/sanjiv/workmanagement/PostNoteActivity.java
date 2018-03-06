package com.example.sanjiv.workmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class PostNoteActivity extends AppCompatActivity {

    private EditText etcat,ettit,etdesc;
    private Button btnAddPost;
    private TextView tvmessage;
        private DatabaseReference mdatabase;
    private ProgressDialog mprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_note);
        btnAddPost = (Button) findViewById(R.id.btnAddPost);
        mprogress = new ProgressDialog(this);

        etcat = (EditText) findViewById(R.id.etCategory);
        ettit = (EditText) findViewById(R.id.etTitle);
        etdesc = (EditText) findViewById(R.id.etDescription);
        tvmessage = (TextView) findViewById(R.id.tvmessage);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Notes");

       btnAddPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });


    }


    private void startPosting() {
        mprogress.setMessage("Adding Note");
        mprogress.show();
        String CategoryValue = etcat.getText().toString().trim();
        String TitleValue = ettit.getText().toString().trim();
        String DescriptionValue = etdesc.getText().toString().trim();

        if (!TextUtils.isEmpty(CategoryValue) && !TextUtils.isEmpty(TitleValue) && !TextUtils.isEmpty(DescriptionValue))
        {

                DatabaseReference newNote = mdatabase.push();
                newNote.child("Category").setValue(CategoryValue);
                newNote.child("Title").setValue(TitleValue);
                newNote.child("Description").setValue(DescriptionValue);
                mprogress.dismiss();
                startActivity(new Intent(PostNoteActivity.this,MainActivity.class));

        }
        else
        {
            mprogress.dismiss();
            tvmessage.setText("* All the Fields are Required ");
        }

    }
}
