package com.mayank.inventory;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.recyclerViewholder> {
    private ArrayList<String>name=new ArrayList<>();
    private ArrayList<String>bcode=new ArrayList<>();
    private ArrayList<String>descp=new ArrayList<>();
    public RecyclerViewAdapter(ArrayList<String> bcode,ArrayList<String> name, ArrayList<String> descp)
    {
        this.bcode=bcode;
        this.name=name;
        this.descp=descp;
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
        if(bcode.get(i)!=null) {
            String barcode = bcode.get(i);
            String Name = name.get(i);
            String descpt = descp.get(i);
            recyclerViewholder.bcode.setText(barcode);
            recyclerViewholder.name.setText(Name);
            recyclerViewholder.descp.setText(descpt);
        }
        }

    @Override
    public int getItemCount() {
        return bcode.size();
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
