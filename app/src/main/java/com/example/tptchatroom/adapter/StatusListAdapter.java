package com.example.tptchatroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devlomi.circularstatusview.CircularStatusView;
import com.example.tptchatroom.ChatActivity;
import com.example.tptchatroom.R;
import com.example.tptchatroom.model.statusmodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StatusListAdapter extends RecyclerView.Adapter<StatusListAdapter.ViewHolder> {
    Context context;
    ArrayList<statusmodel> statuslist;

       public  StatusListAdapter(Context context,ArrayList<statusmodel> statuslist)
       {
           this.context=context;
           this.statuslist=statuslist;
       }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(context).inflate(R.layout.status_sampledesign,parent,false);
       return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         holder.statususername.setText(statuslist.get(position).username);
         holder.statususertime.setText(statuslist.get(position).statusdate);
         holder.circularstatus.setPortionsCount(statuslist.get(position).status.size());
         int statussize=statuslist.get(position).status.size();
        Picasso.get().load(statuslist.get(position).status.get(statussize-1)).placeholder(R.drawable.default_user_icon).into(holder.statususerimg);

        //show stories on click of user status icon
        holder.statususerimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<MyStory> myStories = new ArrayList<>();

                for(String story: statuslist.get(position).status){
                    myStories.add(new MyStory(
                            story
                    ));
                }
                new StoryView.Builder(((ChatActivity)context).getSupportFragmentManager())
                        .setStoriesList(myStories) // Required
                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                        .setTitleText(statuslist.get(position).username) // Default is Hidden
                        .setSubtitleText(statuslist.get(position).statusdate) // Default is Hidden
                        .setTitleLogoUrl(statuslist.get(position).status.get(statussize-1))
                        .setStoryClickListeners(new StoryClickListeners() {
                            @Override
                            public void onDescriptionClickListener(int position) {
                                //your action
                            }

                            @Override
                            public void onTitleIconClickListener(int position) {
                                //your action
                            }
                        }) // Optional Listeners
                        .build() // Must be called before calling show method
                        .show();
            }
        });
       }

    @Override
    public int getItemCount() {
        return statuslist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
      TextView statususername,statususertime;
      CircularStatusView circularstatus;
      ImageView statususerimg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            statususername=itemView.findViewById(R.id.statususername);
            statususertime=itemView.findViewById(R.id.statususertime);
            circularstatus=itemView.findViewById(R.id.circular_status_view);
            statususerimg=itemView.findViewById(R.id.statususerimg);
        }
    }

}
