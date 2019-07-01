package com.mayank.inventory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class AddStock extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore db;
    String bcode;
    Spinner spinner;
    IntentIntegrator qrScan;
    ItemModel itemModel;
    int pos;
     ArrayList<String> types=new ArrayList<>();
     ArrayList<ItemModel> itemModels=new ArrayList<>();
     String type=new String();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);
        initView();
        initData();
        initListener();

        db.collection("Items")
        .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {     int i=0;
                            for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
                            {
                                itemModels.add(new ItemModel(queryDocumentSnapshot.getData().get("Name").toString(),queryDocumentSnapshot
                                        .getData().get("Type").toString(),queryDocumentSnapshot.getData().get("SKU").toString()));
                                i++;
                            }
                          recyclerView.setAdapter(new RecyclerViewAdapterStock(itemModels));
                    }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddStock.this,"Unable to get Data",Toast.LENGTH_LONG).show();

                    }
                });


    }

    private void initListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                type=adapterView.getItemAtPosition(i).toString();
                if(type.equalsIgnoreCase("All")) {
                    recyclerView.setAdapter(new RecyclerViewAdapterStock(itemModels));
                }
                else
                {
                    ArrayList<ItemModel> itemModels1=new ArrayList<>();
                    for(int k=0;k<itemModels.size();k++)
                        if(type.equalsIgnoreCase(itemModels.get(k).getType()))
                                            itemModels1.add(new ItemModel(itemModels.get(k).getName(),itemModels.get(k).getType(),itemModels.get(k).getSKU()));
                            recyclerView.setAdapter(new RecyclerViewAdapterStock(itemModels1));

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


    }

    private void initData() {
       db=FirebaseFirestore.getInstance();
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      qrScan=new IntentIntegrator(this);
       types.add("All");
       type="All";

      db.collection("Type")
              .get()
              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      if(task.isSuccessful())
                      {
                          for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
                          {
                              types.add(queryDocumentSnapshot.getData().get("Key").toString());
                          }
                          ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddStock.this, android.R.layout.simple_spinner_item, types);
                          dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                          spinner.setAdapter(dataAdapter);
                      }

                  }
              });

    }

    private void initView() {
        recyclerView=findViewById(R.id.recycler_view_add_stock);
        spinner=findViewById(R.id.Type_Filter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result not found", Toast.LENGTH_LONG).show();
                return;
            } else {

               bcode = result.getContents();
               String sku=itemModels.get(pos).getSKU();
               Item item=new Item();
               item.putStock(bcode,sku);


            }
        }
    }




    public class RecyclerViewAdapterStock extends RecyclerView.Adapter<RecyclerViewAdapterStock.recyclerViewHolder> {
        private ArrayList<ItemModel> itemModels;
        public RecyclerViewAdapterStock(ArrayList<ItemModel> itemModels) {
            this.itemModels = itemModels;
        }

        @NonNull
        @Override
        public recyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
            View view=layoutInflater.inflate(R.layout.list_item_name_view,parent,false);
            return  new recyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull recyclerViewHolder holder, int position) {
             itemModel=itemModels.get(position);
             if(itemModel.getSKU()!=null) {
                    holder.name.setText(itemModel.getName());
                    holder.sku.setText(itemModel.getSKU());
                    holder.type.setText(itemModel.getType());

                }
        }



        @Override
        public int getItemCount() {
            return itemModels.size();
        }
        public class recyclerViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView sku;
            TextView type;
            public recyclerViewHolder(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.tvStockName);
                sku=itemView.findViewById(R.id.tvSKU);
                type=itemView.findViewById(R.id.tvStockType);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pos=getLayoutPosition();
                        qrScan.initiateScan();

                    }
                });
            }

        }
    }
}