package com.unipi.tinampiniari.wallchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button RegisterButton;
    private EditText UserEmail,UserPassword;
    private TextView AlreadyHaveAccount;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        RegisterButton=findViewById( R.id.register_button );
        UserEmail=findViewById( R.id.register_email );
        UserPassword=findViewById( R.id.register_password );
        AlreadyHaveAccount=findViewById( R.id.already_account );
        mAuth=FirebaseAuth.getInstance();
        dbRef=FirebaseDatabase.getInstance().getReference();

        AlreadyHaveAccount.setOnClickListener( new View.OnClickListener() { //αν ο χρήστης διαθέτει λογαριασμό,κατευθύνεται στην Login σελίδα
            @Override
            public void onClick(View v) {
                gotoLoginActivity();
            }
        } );

        RegisterButton.setOnClickListener( new View.OnClickListener() { //εγγραφή χρήστη
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        } );

    }
        private void CreateAccount(){
            String email=UserEmail.getText().toString();
            String password=UserPassword.getText().toString();

            if(TextUtils.isEmpty( email)){
                Toast.makeText( this,"Please enter an email",Toast.LENGTH_LONG ).show();
            }
            if(TextUtils.isEmpty( password)){
                Toast.makeText( this,"Please enter a password",Toast.LENGTH_LONG ).show();
            }else{
                mAuth.createUserWithEmailAndPassword( email,password ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    //εγγραφή στην βάση
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String currentUserId=mAuth.getCurrentUser().getUid();
                            dbRef.child( "Users" ).child( currentUserId ).setValue( "" );
                            gotoMainActivity();
                            Toast.makeText( RegisterActivity.this,"Account Created Succefully",Toast.LENGTH_LONG ).show();
                        }else {
                            String message=task.getException().toString();
                            Toast.makeText( RegisterActivity.this,"Error"+ message,Toast.LENGTH_LONG ).show();

                        }
                    }
                } );
            }


        }

    private void gotoLoginActivity() {
        Intent loginIntent=new Intent( RegisterActivity.this,LoginActivity.class );
        startActivity( loginIntent );
    }


    private void gotoMainActivity() {
        Intent mainIntent=new Intent( RegisterActivity.this,MainActivity.class );
        mainIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //the user can't go back if he press the back button
        startActivity( mainIntent );
        finish();
    }
}
