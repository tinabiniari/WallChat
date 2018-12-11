package com.unipi.tinampiniari.wallchat;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> messagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;



    public  MessageAdapter(List<Messages> messagesList)
    {
            this.messagesList=messagesList;

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder
    {

        public TextView senderText,receiverText,senderName,receiverDate,receiverTime,senderTime;

        public MessageViewHolder(@NonNull View itemView) {
            super( itemView );

            receiverText=itemView.findViewById( R.id.receiver_message_text );
            senderText=itemView.findViewById( R.id.sender_messsage_text );
            senderName=itemView.findViewById( R.id.sender_name );

            receiverDate=itemView.findViewById( R.id.receiver_date );
            receiverTime=itemView.findViewById( R.id.receiver_time );
            senderTime=itemView.findViewById( R.id.sender_time );


        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view=LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.messages_layout,viewGroup,false );
        mAuth=FirebaseAuth.getInstance();
        return  new MessageViewHolder( view );

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        String messageSenderId=mAuth.getCurrentUser().getUid();
        Messages messages=messagesList.get( i );
        String fromUserName=messages.getName();
        String messageDate=messages.getDate();
        String messageTime=messages.getTime();
        String fromUserID=messages.getId();

        usersRef=FirebaseDatabase.getInstance().getReference().child( "Chat" );


        if(messageSenderId.equals( fromUserID )){  //αν το μήνυμα που έχει σταλεί είναι από τον τρέχον χρήστη
            messageViewHolder.receiverText.setVisibility( View.INVISIBLE );
            messageViewHolder.senderText.setVisibility( View.VISIBLE );

            messageViewHolder.senderText.setBackgroundResource( R.drawable.sender_messages_layout );
            messageViewHolder.senderText.setText( messages.getMessage() );
            messageViewHolder.senderName.setVisibility( View.INVISIBLE );
            messageViewHolder.receiverDate.setVisibility( View.INVISIBLE );
            messageViewHolder.receiverTime.setVisibility( View.INVISIBLE );
            messageViewHolder.senderTime.setVisibility( View.VISIBLE );
            messageViewHolder.senderTime.setText( messages.getTime() );


        }

        else{
            messageViewHolder.senderText.setVisibility( View.INVISIBLE );
            messageViewHolder.receiverText.setVisibility( View.VISIBLE );

            messageViewHolder.receiverText.setBackgroundResource( R.drawable.receiver_messages_layout );
            messageViewHolder.receiverText.setText( messages.getMessage() );

            messageViewHolder.senderName.setVisibility( View.VISIBLE );
            messageViewHolder.receiverDate.setVisibility( View.VISIBLE );
            messageViewHolder.receiverTime.setVisibility( View.VISIBLE );

            messageViewHolder.receiverDate.setText( messages.getDate() );
            messageViewHolder.receiverTime.setText( messages.getTime() );

            messageViewHolder.senderTime.setVisibility( View.INVISIBLE );




            messageViewHolder.senderName.setText( messages.getName() );




        }





        }



    @Override
    public int getItemCount() {
        return messagesList.size();
    }



}
