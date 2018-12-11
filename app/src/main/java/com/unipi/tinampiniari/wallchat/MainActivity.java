package com.unipi.tinampiniari.wallchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private ImageButton startChatButton,settingsButton,exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        dbRef=FirebaseDatabase.getInstance().getReference();
        startChatButton=findViewById( R.id.start_chat );
        settingsButton=findViewById( R.id.settings_btn );
        exitButton=findViewById( R.id.logout_btn );

        settingsButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSettings();
            }
        } );

        startChatButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoChatPage();
            }
        } );

        exitButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                gotoLogin();
            }
        } );




    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser == null){
            gotoLogin();

        }
        else {
            verifyUser();
        }
    }

    private void gotoLogin() {
        Intent loginIntent=new Intent( MainActivity.this,LoginActivity.class );
        loginIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //the user can't go back if he press the back button

        startActivity( loginIntent );
        finish();
    }

    private void gotoChatPage(){
        Intent chatIntent=new Intent( MainActivity.this,ChatActivity2.class );
        startActivity( chatIntent );
    }

    private void gotoSettings() {
        Intent settingsIntent=new Intent( MainActivity.this,SettingsActivity.class );
        settingsIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //the user can't go back if he press the back button

        startActivity( settingsIntent );
        finish();
    }
    private void verifyUser(){
        String currentUserId=mAuth.getCurrentUser().getUid();

        dbRef.child( "Users" ).child( currentUserId ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child( "name" ).exists()){
                    Toast.makeText( MainActivity.this,"Welcome",Toast.LENGTH_LONG ).show();
                    }
                    else {
                    gotoSettings();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu( menu );
        getMenuInflater().inflate( R.menu.options_menu,menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected( item );

        if(item.getItemId()==R.id.logout_option){
            mAuth.signOut();
            gotoLogin();
        }
        if(item.getItemId()==R.id.setting_option){
            gotoSettings();
        }

        return true;
    }
}
