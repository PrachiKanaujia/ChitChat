package com.example.tptchatroom.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tptchatroom.MessageDetail;
import com.example.tptchatroom.R;
import com.example.tptchatroom.model.usermodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{
    Context context;
    ArrayList<usermodel> list;
    ImageView userprofilepic;
    TextView username,userlastmsg;

    public UserListAdapter(Context context,ArrayList<usermodel> list)
    {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.sampleuser_design,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        usermodel user=list.get(position);
        username.setText(user.name);
        userlastmsg.setText(user.email);
        Picasso.get().load(user.profilepic).placeholder(R.drawable.default_user_icon).into(userprofilepic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, MessageDetail.class);
                intent.putExtra("name",user.name);
                intent.putExtra("uid",user.uid);
                context.startActivity(intent);
            }
        });
    }

//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//       usermodel user=list.get(position);
//       username.setText(user.name);
//       userlastmsg.setText(user.email);
//    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        public ViewHolder(View itemView){
            super(itemView);
            userprofilepic=itemView.findViewById(R.id.userprofilepic);
            username=itemView.findViewById(R.id.username);
            userlastmsg=itemView.findViewById(R.id.userlastmsg);

        }
    }

}
