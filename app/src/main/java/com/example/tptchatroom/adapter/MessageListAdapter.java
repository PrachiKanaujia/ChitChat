package com.example.tptchatroom.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tptchatroom.R;
import com.example.tptchatroom.model.messagemodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter {
  Context context;
  ArrayList<messagemodel> msglist;
  String senderreceiver;
  public MessageListAdapter(Context context,  ArrayList<messagemodel> msglist,String senderreceiver)
    {
        this.context=context;
        this.msglist=msglist;
        this.senderreceiver=senderreceiver;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(viewType==1) {
           View v = LayoutInflater.from(context).inflate(R.layout.sendermsg_sampledesign, parent, false);
           return new SenderViewHolder(v);
       }
       else
       {
           View v=LayoutInflater.from(context).inflate(R.layout.receivermsg_sampledesign,parent,false);
           return new ReceiverViewHolder(v);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getClass()==SenderViewHolder.class)
        {
            ((SenderViewHolder) holder).sendermsg.setText(msglist.get(position).msg);
            ((SenderViewHolder) holder).sendermsgtime.setText(msglist.get(position).msgtime);
        }
        else
        {
            ((ReceiverViewHolder) holder).receivermsg.setText(msglist.get(position).msg);
            ((ReceiverViewHolder) holder).receivermsgtime.setText(msglist.get(position).msgtime);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
               //Toast.makeText(context,msglist.get(holder.getAdapterPosition()).messagekey+"", Toast.LENGTH_LONG).show();
                AlertDialog.Builder alert=new AlertDialog.Builder(context);
                alert.setTitle("Delete Message");
                alert.setMessage("Do you really want to delete the message?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       // Toast.makeText(context,senderreceiver,Toast.LENGTH_LONG).show();
                        // Toast.makeText(context, msglist.get(position).messagekey+"", Toast.LENGTH_LONG).show();
                        FirebaseDatabase.getInstance().getReference().child("message").child(senderreceiver).child(msglist.get(holder.getAdapterPosition()).messagekey).setValue(null);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                          dialogInterface.dismiss();
                    }
                });
                alert.show();
                return false;
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        if(msglist.get(position).senderid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            return 1;
        }
        else
        {
            return 2;
        }
    }

    @Override
    public int getItemCount()
    {
        return msglist.size();
    }

    //hold reference of sender message and sendermsgtime Textview from sendermsg sampledesign
    class  SenderViewHolder extends RecyclerView.ViewHolder
    {
        TextView sendermsg,sendermsgtime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            sendermsg=itemView.findViewById(R.id.sendermsg);
            sendermsgtime=itemView.findViewById(R.id.sendermsgtime);

        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder
    {
        TextView receivermsg,receivermsgtime;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receivermsg=itemView.findViewById(R.id.receivermsg);
            receivermsgtime=itemView.findViewById(R.id.receivermsgtime);
        }
    }
}
