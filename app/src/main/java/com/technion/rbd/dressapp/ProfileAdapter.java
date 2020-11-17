package com.technion.rbd.dressapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technion.rbd.dressapp.FrontEnd.Item;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ProfileItems> mArrayList;

    ProfileAdapter(Context mContext, ArrayList<ProfileItems> al) {
        this.mContext = mContext;
        mArrayList = al;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.profile_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProfileItems profileItem = mArrayList.get(position);
        ImageView image = holder.itemImage;
        TextView clothesName = holder.clothesName;
//        TextView bustSize = holder.bustSize;
//        TextView waistSize = holder.waistSize;
//        TextView length = holder.length;
        TextView itemId = holder.hItemId;

        //image.setImageResource(profileItem.getImage());
        Picasso.get().load(profileItem.getImageUrl()).into(image);
        clothesName.setText(profileItem.getClothesName());
//        bustSize.setText(profileItem.getBustSize());
//        waistSize.setText(profileItem.getWaistSize());
//        length.setText(profileItem.getLength());
        itemId.setText(profileItem.getItemId());

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView clothesName, bustSize, waistSize, length;
        TextView hItemId;

        public ViewHolder(View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.profileItemPic);
            clothesName = itemView.findViewById(R.id.profileItemName);
//            bustSize = itemView.findViewById(R.id.profileItemBustSize);
//            waistSize = itemView.findViewById(R.id.profileItemWaistSize);
//            length = itemView.findViewById(R.id.profileItemLength);

            //FrontEnd Part Below
            hItemId = itemView.findViewById(R.id.hItemId);

        }

        public TextView getClothesName() {
            return clothesName;
        }

        public String gethItemId() {
            return hItemId.getText().toString();
        }
    }
}
