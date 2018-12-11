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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private Button LoginButton;
    private EditText UserEmail,UserPassword;
    private TextView NeedNewAccount;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        LoginButton=findViewById( R.id.login_button );
        UserEmail=findViewById( R.id.login_email );
        UserPassword=findViewById( R.id.login_password );
        NeedNewAccount=findViewById( R.id.need_account );
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        NeedNewAccount.setOnClickListener( new View.OnClickListener() { //αν ο χρήστης δεν έχει λογαριασμό,κατευθύνεται σε εγγραφή
            @Override
            public void onClick(View v) {
                gotoRegisterActivity();
            }
        } );

        LoginButton.setOnClickListener( new View.OnClickListener() { //σύνδεση του χρήστη
            @Override
            public void onClick(View v) {
                userLogin();
            }
        } );
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser!=null){
            gotoMainActivity();
        }
    }

    private void userLogin(){

        String email=UserEmail.getText().toString();
        String password=UserPassword.getText().toString();

        if(TextUtils.isEmpty( email)){
            Toast.makeText( this,"Please enter an email",Toast.LENGTH_LONG ).show();
        }
        if(TextUtils.isEmpty( password)){
            Toast.makeText( this,"Please enter a password",Toast.LENGTH_LONG ).show();
        }else{
            mAuth.signInWithEmailAndPassword( email,password ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        gotoMainActivity();
                        Toast.makeText( LoginActivity.this,"You are logged in!",Toast.LENGTH_LONG ).show();

                    }else {
                        String message=task.getException().toString();
                        Toast.makeText( LoginActivity.this,"Error : "+ message,Toast.LENGTH_LONG ).show();

                    }
                }
            } );
        }

    }

    private void gotoMainActivity() {
        Intent loginIntent=new Intent( LoginActivity.this,MainActivity.class );
        loginIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //the user can't go back if he press the back button
        startActivity( loginIntent );
        finish();
    }

    private void gotoRegisterActivity() {
        Intent registerIntent=new Intent( LoginActivity.this,RegisterActivity.class );
        startActivity( registerIntent );
    }
}
