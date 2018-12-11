package com.unipi.tinampiniari.wallchat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ChatActivity2 extends AppCompatActivity {




    private ImageButton sendMessage;
    private EditText inputMessage;
    private String currentUserId,currentUsername,currentDate,currentTime;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef,messageKeyRef,chatRef;

    private final List<Messages> messagesList=new ArrayList<>();
    private MessageAdapter messageAdapter=new MessageAdapter( messagesList );
    private RecyclerView userMessagesList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_chat2 );

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        usersRef=FirebaseDatabase.getInstance().getReference().child( "Users" );
        chatRef=FirebaseDatabase.getInstance().getReference().child( "Chat" );


        sendMessage=findViewById( R.id.send_message );
        inputMessage=findViewById( R.id.input_message_text );
        userMessagesList=findViewById( R.id.message_list );


         userMessagesList.setLayoutManager( new LinearLayoutManager( this ) );
         userMessagesList.setAdapter( new MessageAdapter( messagesList ) );

        getUserInfo();


        sendMessage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfoToDb();
                inputMessage.setText( "" );




            }
        } );

        chatRef.addChildEventListener( new ChildEventListener() { //αν η βάση έχει ενημερωθεί,εμφάνιση των μηνυμάτων
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    Messages messages=dataSnapshot.getValue(Messages.class);
                    messagesList.add( messages );
                    userMessagesList.scrollToPosition( messagesList.size() -1 );

                    messageAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    Messages messages=dataSnapshot.getValue(Messages.class);
                    messagesList.add( messages );
                    userMessagesList.scrollToPosition( messagesList.size() -1 );

                    messageAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void getUserInfo(){
        usersRef.child( currentUserId ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    currentUsername=dataSnapshot.child( "name" ).getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    private void saveInfoToDb()

    {

        String message=inputMessage.getText().toString();
        String messageKey=chatRef.push().getKey();

        if(TextUtils.isEmpty( message ))
        {
            Toast.makeText( this,"Please write something" ,Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar date=Calendar.getInstance();
            SimpleDateFormat dateFormat=new SimpleDateFormat( "dd/MM/yyyy" );
            currentDate=dateFormat.format(date.getTime());

            Calendar time=Calendar.getInstance();
            SimpleDateFormat timeFormat=new SimpleDateFormat( "hh:mm a" );
            currentTime=timeFormat.format(time.getTime());

            HashMap<String,Object> groupMessageKey=new HashMap<>(  );
            chatRef.updateChildren( groupMessageKey );

            messageKeyRef=chatRef.child( messageKey );

            HashMap<String,Object> messageInfoMap=new HashMap<>(  );
            messageInfoMap.put( "name",currentUsername );
            messageInfoMap.put( "message",message );
            messageInfoMap.put("date",currentDate);
            messageInfoMap.put( "time",currentTime );
            messageInfoMap.put("id",currentUserId);

            messageKeyRef.updateChildren( messageInfoMap );


        }
    }


}
