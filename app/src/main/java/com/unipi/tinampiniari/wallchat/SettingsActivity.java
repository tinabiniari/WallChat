package com.unipi.tinampiniari.wallchat;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private Button updateAccount,cancel;
    private EditText username;

    private String currentID;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_settings );
        updateAccount=findViewById( R.id.update );
        cancel=findViewById( R.id.cancel );

        username=findViewById( R.id.set_username );

        mAuth=FirebaseAuth.getInstance();
        currentID=Objects.requireNonNull( mAuth.getCurrentUser() ).getUid();
        dbRef=FirebaseDatabase.getInstance().getReference();

        updateAccount.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
            }
        } );


        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMainActivity();
            }
        } );
        retrieveUserInfo();

    }

    private void updateSettings() {
        String setUsername=username.getText().toString();

        if (TextUtils.isEmpty( setUsername )) {
            Toast.makeText( this,"Please set a username",Toast.LENGTH_LONG ).show();
        }
        else {
            HashMap<String,String> profile=new HashMap<>(  );
            profile.put("uid",currentID);
            profile.put( "name",setUsername );
            dbRef.child( "Users" ).child( currentID ).setValue( profile ).addOnCompleteListener( new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        gotoMainActivity();
                        Toast.makeText(SettingsActivity.this,"Updated Succesfully",Toast.LENGTH_LONG).show();

                    }
                    else {
                        String message=task.getException().toString();
                        Toast.makeText(SettingsActivity.this,"Error : "+message,Toast.LENGTH_LONG).show();

                    }

                }
            } );
        }
    }

        private void retrieveUserInfo(){
        dbRef.child( "Users" ).child( currentID ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.exists()) && (dataSnapshot.hasChild( "name" ))){
                    String retrieveUsername=dataSnapshot.child( "name" ).getValue().toString();

                    username.setText( retrieveUsername );
                }
                else {
                    Toast.makeText( SettingsActivity.this,"Please set a username",Toast.LENGTH_LONG ).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        }

    private void gotoMainActivity() {
        Intent loginIntent=new Intent( SettingsActivity.this,MainActivity.class );
        loginIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //the user can't go back if he press the back button
        startActivity( loginIntent );
        finish();
    }
}
