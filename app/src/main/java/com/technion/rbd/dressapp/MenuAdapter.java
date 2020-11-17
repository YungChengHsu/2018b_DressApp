package com.technion.rbd.dressapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technion.rbd.dressapp.BackEnd.FirebaseCallback;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MenuItems> mArrayList;

    MenuAdapter(Context mContext, ArrayList<MenuItems> al) {
        this.mContext = mContext;
        mArrayList = al;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.menu_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MenuItems menuItem = mArrayList.get(position);
        ImageView itemImage = holder.itemImage;
        ImageView ownerImage = holder.ownerImage;
        TextView clothesName = holder.clothesName;
//        TextView bustSize = holder.bustSize;
//        TextView waistSize = holder.waistSize;
//        TextView length = holder.length;
        TextView ownerName = holder.ownerName;
        TextView ownerLocation = holder.ownerLocation;

        final String item_idx = menuItem.getItemId();
        Picasso.get().load(menuItem.getItemImageUrl()).into(itemImage);
        Picasso.get().load(menuItem.getOwnerImage()).into(ownerImage);
        clothesName.setText(menuItem.getClothesName());
//        bustSize.setText(menuItem.getBustSize());
//        waistSize.setText(menuItem.getWaistSize());
//        length.setText(menuItem.getLength());
        ownerName.setText(menuItem.getOwnerName());
        ownerLocation.setText(menuItem.getOwnerLocation());

        if (item_idx == null) {
            Log.e("BackEnd error logx", "item id = null");
        }
        View.OnClickListener clicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ItemViewActivity.class);
                intent.putExtra("itemId", item_idx);
                mContext.startActivity(intent);

            }
        };
        itemImage.setOnClickListener(clicker);

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage, ownerImage;
        TextView clothesName, bustSize, waistSize, length, ownerName, ownerLocation;

        public ViewHolder(View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.menuItemPic);
            ownerImage = itemView.findViewById(R.id.menuOwnerPic);
            clothesName = itemView.findViewById(R.id.menuItemName);
//            bustSize = itemView.findViewById(R.id.menuItemBustSize);
//            waistSize = itemView.findViewById(R.id.menuItemWaistSize);
//            length = itemView.findViewById(R.id.menuItemLength);
            ownerName = itemView.findViewById(R.id.menuOwnerName);
            ownerLocation = itemView.findViewById(R.id.menuOwnerLocation);

        }
    }
}
