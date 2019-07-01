package com.mayank.inventory;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemViewRecyclerViewAdapter extends RecyclerView.Adapter<ItemViewRecyclerViewAdapter.recyclerViewholder> {
    private ArrayList<ItemModel>itemList;
    public ItemViewRecyclerViewAdapter(ArrayList<ItemModel>itemList)
    {
        this.itemList = itemList;
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
            recyclerViewholder.bcode.setText("Barcode : "+itemModel.getSKU());
            recyclerViewholder.name.setText("Name : "+itemModel.getName());
            recyclerViewholder.descp.setText("Description : "+itemModel.getType());
        }
        }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class recyclerViewholder extends RecyclerView.ViewHolder {
         TextView bcode;
         TextView name;
         TextView descp;
         public recyclerViewholder(@NonNull View itemView) {
            super(itemView);
            bcode =itemView.findViewById(R.id.tvBcode);
            name =itemView.findViewById(R.id.tvName);
            descp =itemView.findViewById(R.id.tvDescp);
        }
    }
}
