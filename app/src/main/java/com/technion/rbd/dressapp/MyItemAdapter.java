package com.technion.rbd.dressapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyItemAdapter extends RecyclerView.Adapter<MyItemAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MyOrderItems> mArrayList;

    MyItemAdapter(Context mContext, ArrayList<MyOrderItems> al) {
        this.mContext = mContext;
        mArrayList = al;
    }

    @NonNull
    @Override
    public MyItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.my_item_items, parent, false);

        MyItemAdapter.ViewHolder viewHolder = new MyItemAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemAdapter.ViewHolder holder, int position) {

        MyOrderItems myOrderItem = mArrayList.get(position);
        ImageView itemImage = holder.itemImage;
        //ImageView ownerIamge = holder.ownerImage;
        TextView clothesName = holder.clothesName;
//        TextView bustSize = holder.bustSize;
//        TextView waistSize = holder.waistSize;
//        TextView length = holder.length;
        //TextView ownerName = holder.ownerName;
        //TextView ownerLocation = holder.ownerLocation;
        TextView status = holder.status;

//        final String item_idx = myOrderItem.getItemId();
//        Picasso.get().load(myOrderItem.getItemImageUrl()).into(itemImage);
//        Picasso.get().load(myOrderItem.getOwnerImage()).into(ownerIamge);

        itemImage.setImageResource(R.drawable.closet);
        //ownerIamge.setImageResource(myOrderItem.getOwnerImage());
        clothesName.setText(myOrderItem.getClothesName());
//        bustSize.setText(menuItem.getBustSize());
//        waistSize.setText(menuItem.getWaistSize());
//        length.setText(menuItem.getLength());
        //ownerName.setText(myOrderItem.getOwnerName());
        //ownerLocation.setText(myOrderItem.getOwnerLocation());
        status.setText(myOrderItem.getStatus());

//        if (item_idx == null) {
//            Log.e("BackEnd error logx", "item id = null");
//        }
//        View.OnClickListener clicker = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, ItemViewActivity.class);
//                intent.putExtra("itemId", item_idx);
//                mContext.startActivity(intent);
//
//            }
//        };
//        itemImage.setOnClickListener(clicker);

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;//, ownerImage;
        TextView clothesName, bustSize, waistSize, length, status; //ownerName, ownerLocation, status;

        public ViewHolder(View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.myItemItemPic);
            //ownerImage = itemView.findViewById(R.id.myOrderOwnerPic);
            clothesName = itemView.findViewById(R.id.myItemItemName);
//            bustSize = itemView.findViewById(R.id.menuItemBustSize);
//            waistSize = itemView.findViewById(R.id.menuItemWaistSize);
//            length = itemView.findViewById(R.id.menuItemLength);
            //ownerName = itemView.findViewById(R.id.myOrderOwnerName);
            //ownerLocation = itemView.findViewById(R.id.myOrderOwnerLocation);
            status = itemView.findViewById(R.id.myItemStatus);
        }
    }
}
