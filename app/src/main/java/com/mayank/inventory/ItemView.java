package com.mayank.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

public class ItemView extends AppCompatActivity {


    FirebaseFirestore db;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    FloatingActionButton floatingActionButton;
    StorageReference storageReference;
    FirebaseStorage storage;
    Integer index;
    ArrayList<ItemModel>itemList = new ArrayList<>();
    ArrayList<String>imageList = new ArrayList<>();
    ItemViewRecyclerViewAdapter itemViewRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        initViews();
        initData();
        initListener();
        toolbar.setTitle("Items");
        setSupportActionBar(toolbar);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(itemViewRecyclerViewAdapter);
       progressDialog.setMessage("Loading Content,Please Wait....");
       progressDialog.show();
        db.collection("Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      try {


                        if (task.isSuccessful()) {

                            itemList.clear();
                            int i = 0;
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("Status").toString().equalsIgnoreCase("1"))
                                {

                                  //  Log.e("DAta", document.getData().get("Name").toString());
                                     // Log.i("Mayank",itemList.get(index).getSKU());
                                    Log.i("Indexoutside", ""+i);
                                    storageReference.child("ItemImages/" + document.getData().get("SKU").toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            /*Log.i("Index", ""+index);
                                            itemList.get(index).setImageUrl(uri.toString());
                                            imageList.add(uri.toString());
                                            Log.i("Mayank", uri.toString());
                                            Log.i("Index", ""+index);
                                            itemViewRecyclerViewAdapter.notifyDataSetChanged();
                                            index++;
                                            if(imageList.size() > 0) {
                                                for (ItemModel model : itemList) {
                                                    Log.e("imageModel", "" + model.getImageUrl());
                                                }
                                            }*/
                                            itemList.add(new ItemModel(document.getData().get("Name").toString(), document.getData().get("Type").toString(),document.getData().get("SKU").toString(), uri.toString()));
                                            Collections.sort(itemList);
                                            itemViewRecyclerViewAdapter.notifyDataSetChanged();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("Mayank", "Error Getting Images");
                                        }
                                    });

                                }

                                i++;
                            }



                            progressDialog.dismiss();





                        }
                      }   catch (Exception e){
                            e.printStackTrace();
                            Log.w("tag", "Error getting documents.", task.getException());
                           }
                    }
                });



    }




    private void initListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ItemView.this,AddInventory.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void initData() {
        db=FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(this);
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        index=0;
        itemViewRecyclerViewAdapter= new ItemViewRecyclerViewAdapter(ItemView.this,itemList);


    }

    private void initViews() {
        floatingActionButton=findViewById(R.id.f1);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        toolbar =findViewById(R.id.generalToolbar);
    }
}
