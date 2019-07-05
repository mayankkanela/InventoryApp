package com.mayank.inventory;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;


import java.util.ArrayList;

public class ItemViewRecyclerViewAdapter extends RecyclerView.Adapter<ItemViewRecyclerViewAdapter.recyclerViewholder> {
    private ArrayList<ItemModel>itemList;

    Activity activity;
    public ItemViewRecyclerViewAdapter(Activity activity,ArrayList<ItemModel>itemList)
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
        }
    }


}

