package com.example.tptchatroom;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tptchatroom.adapter.MessageListAdapter;
import com.example.tptchatroom.model.messagemodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class MessageDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        getSupportActionBar().hide();
        Intent intent=getIntent();
        TextView username=findViewById(R.id.username);
        username.setText(intent.getStringExtra("name"));

        String receiveruid=intent.getStringExtra("uid");
        String senderuid= FirebaseAuth.getInstance().getCurrentUser().getUid();
       // Toast.makeText(this, receiveruid+ " "+senderuid, Toast.LENGTH_LONG).show();

        ImageView back=findViewById(R.id.btnback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MessageDetail.this,ChatActivity.class);
                startActivity(in);
            }
        });

        //let's save messages in Firebase Database on click of send button
        ImageView sendbtn= findViewById(R.id.sendmsg);
        EditText message=findViewById(R.id.usermsg);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(!message.getText().toString().trim().isEmpty())
                {
                   messagemodel md=new messagemodel();
                   md.msg=message.getText().toString();
                    SimpleDateFormat format=new SimpleDateFormat("hh:mm aa");     //if you want date also then set "mm/dd hh:mm" in pattern
                   Date date=new Date();
                   md.msgtime=String.valueOf(format.format(date));
                   md.senderid=senderuid;
                   FirebaseDatabase.getInstance().getReference().child("message").child(senderuid+receiveruid).push().setValue(md).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           FirebaseDatabase.getInstance().getReference().child("message").child(receiveruid+senderuid).push().setValue(md);
                           message.setText("");
                       }
                   });
                }
                else
                {
                    Toast.makeText(MessageDetail.this, "Enter message", Toast.LENGTH_LONG).show();
                }
            }
        });

        ArrayList<messagemodel> msglist=new ArrayList<>();
    //Let's bind MessageListAdapter to the recyclerview
        RecyclerView recycle=findViewById(R.id.recyclemsg);
        MessageListAdapter adapter=new MessageListAdapter(this,msglist,senderuid+receiveruid);
        recycle.setAdapter(adapter);
        recycle.setLayoutManager(new LinearLayoutManager(this));



    //Let's select all message of sender and receiver from database


        FirebaseDatabase.getInstance().getReference().child("message").child(senderuid+receiveruid).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot)
          {
              msglist.clear();
              for (DataSnapshot ds: snapshot.getChildren())
               {
                   messagemodel m=new messagemodel();
                   m.msg=ds.child("msg").getValue(String.class);
                   m.msgtime=ds.child("msgtime").getValue(String.class);
                   m.senderid=ds.child("senderid").getValue(String.class);
                   m.messagekey=ds.getKey();
                   msglist.add(m);
               }
             adapter.notifyDataSetChanged();
              if(msglist.size()>2)
             recycle.smoothScrollToPosition(msglist.size()-1);             //isse hmare message window me last message pe hme redirect krega
              //Log.v("data",snapshot+"");
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });

    }


}