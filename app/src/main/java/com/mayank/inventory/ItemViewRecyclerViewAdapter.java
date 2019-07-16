package com.mayank.inventory;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

public class ItemViewRecyclerViewAdapter extends RecyclerView.Adapter<ItemViewRecyclerViewAdapter.recyclerViewholder>  {
    private ArrayList<ItemModel>itemList;
    int pos;
     Activity activity;
    public ItemViewRecyclerViewAdapter(Activity activity, ArrayList<ItemModel>itemList)
    {
        this.itemList = itemList;
        this.activity=activity;
    }
    @NonNull
    @Override
    public recyclerViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View view=layoutInflater.inflate(R.layout.list_item_view,viewGroup,false);
        return new recyclerViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewholder recyclerViewholder, int i) {


            ItemModel itemModel = itemList.get(i);
            if(itemModel.getSKU()!=null){
                recyclerViewholder.sku.setText("SKU    :"+ itemModel.getSKU());
                recyclerViewholder.name.setText("Name :"+itemModel.getName());
                recyclerViewholder.type.setText("Type   :"+itemModel.getType());
                if(itemModel.getImageUrl()!=null && !TextUtils.isEmpty(itemModel.getImageUrl()))
                    Glide.with(activity).load(itemModel.getImageUrl()).into(recyclerViewholder.imageView);
                else
                    recyclerViewholder.imageView.setImageDrawable(activity.getDrawable(R.drawable.ic_launcher_foreground));



            }
        }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class recyclerViewholder extends RecyclerView.ViewHolder {
         TextView sku;
         TextView name;
         TextView type;
         ImageView imageView;
         public recyclerViewholder(@NonNull View itemView) {
            super(itemView);
            sku =itemView.findViewById(R.id.tvSKUi);
            name =itemView.findViewById(R.id.tvName);
            type =itemView.findViewById(R.id.tvTypei);
            imageView=itemView.findViewById(R.id.listImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pos=getLayoutPosition();
                    ItemModel itemModel=itemList.get(pos);
                    Intent intent=new Intent(activity,ExpandedItemView.class);
                    intent.putExtra("Item",itemModel);
                    activity.startActivity(intent);
                    activity.finish();


                }
            });


        }
    }


}

