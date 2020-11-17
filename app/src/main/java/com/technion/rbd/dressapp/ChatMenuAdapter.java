package com.technion.rbd.dressapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technion.rbd.dressapp.BackEnd.FirebaseCallback;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMenuAdapter extends RecyclerView.Adapter<ChatMenuAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ChatMessage> mArrayList;

    ChatMenuAdapter(Context mContext, ArrayList<ChatMessage> al) {
        this.mContext = mContext;
        mArrayList = al;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.chat_menu_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ChatMessage menuItem = mArrayList.get(position);
        CircleImageView receiver_img = holder.receiver_img;
        TextView messageText = holder.messageText;
        TextView messageUser = holder.messageUser;
        TextView messageTime = holder.messageTime;

        receiver_img.setImageResource(R.drawable.default_profile_pic);
        messageText.setText(menuItem.getMessageText());
        messageUser.setText(menuItem.getMessageUser());
        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm)",
                menuItem.getMessageTime()));
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView receiver_img;
        TextView messageText;
        TextView messageUser;
        TextView messageTime;

        public ViewHolder(View itemView) {
            super(itemView);

            receiver_img = itemView.findViewById(R.id.chatMenu_pic);
            messageText = itemView.findViewById(R.id.chatMenu_text);
            messageUser = itemView.findViewById(R.id.chatMenu_user);
            messageTime = itemView.findViewById(R.id.chatMenu_time);
        }
    }
}
