package com.example.sanjiv.workmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

          private RecyclerView mPostList;
           private FirebaseAuth mAuth;
//           private  DatabaseReference mDatabaseUsers;
    private  FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       mPostList = (RecyclerView)findViewById(R.id.post_list);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(this));






        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() ==null)
                {
                    Intent loginintent = new Intent(MainActivity.this,LoginActivity.class);
                    loginintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginintent);
                }
            }
        };

            mDatabase = FirebaseDatabase.getInstance().getReference().child("Notes");
//        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
//        mDatabaseUsers.keepSynced(true);
//
//



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected  void onStart()
    {
        super.onStart();

       mAuth.addAuthStateListener(mAuthListener);
//        CheckUserExist();
      FirebaseRecyclerAdapter<Note, NoteViewHolder> FBRA= new FirebaseRecyclerAdapter<Note, NoteViewHolder>(
                Note.class,
                R.layout.note_row,
                NoteViewHolder.class,
                mDatabase
        ) {


            @Override
            protected void populateViewHolder(NoteViewHolder viewHolder, Note model, int position) {
                viewHolder.setCategory(model.getCategory());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());

            }
        };

        mPostList.setAdapter(FBRA);



    }


//    private void CheckUserExist() {
//        final String user_id= mAuth.getCurrentUser().getUid();
//        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (!dataSnapshot.hasChild(user_id))
//                {
//                    Intent setupintent = new Intent(MainActivity.this,SetupActivity.class);
//                    setupintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(setupintent);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }



    public static class NoteViewHolder extends RecyclerView.ViewHolder{


        public NoteViewHolder(View itemView) {
            super(itemView);
            View mview=itemView;
        }
        public  void setCategory(String Category)
        {
            TextView NoteCategory = (TextView) itemView.findViewById(R.id.NoteCategory);
            NoteCategory.setText(Category);
        }

        public  void setTitle(String Title)
        {
            TextView NoteTitle = (TextView) itemView.findViewById(R.id.NoteTitle);
            NoteTitle.setText(Title);
        }
        public  void setDescription(String Description)
        {
            TextView NoteDescription = (TextView) itemView.findViewById(R.id.NoteDescription);
            NoteDescription.setText(Description);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.addnote)
        {

            Intent intent = new Intent(MainActivity.this,PostNoteActivity.class);
            startActivity(intent);
        }
        else if (id==R.id.action_logout)
        {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
