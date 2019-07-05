package com.mayank.inventory;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StockCheckAdapter extends RecyclerView.Adapter<StockCheckAdapter.ViewHolder> {
    private ArrayList<ItemModel> itemList=new ArrayList<>();

    public StockCheckAdapter(@NonNull ArrayList<ItemModel> itemList) {
        this.itemList = itemList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.list_stock_check,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(itemList.get(position).getSKU()!=null&& !TextUtils.isEmpty(itemList.get(position).getSKU()))
        {ItemModel itemModel=itemList.get(position);
          holder.name.setText("Name   :  "+itemModel.getName());
          holder.type.setText("Type     :  "+itemModel.getType());
          holder.count.setText("Count   :  "+itemModel.getCount().toString());
          holder.sku.setText("Sku       :  "+itemModel.getSKU());

        }

    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView sku;
        TextView count;
        TextView type;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tvStockCheckName);
            sku=itemView.findViewById(R.id.tvStockCheckId);
            count=itemView.findViewById(R.id.tvStockCheckCount);
            type=itemView.findViewById(R.id.tvStockCheckType);
        }
    }
}
