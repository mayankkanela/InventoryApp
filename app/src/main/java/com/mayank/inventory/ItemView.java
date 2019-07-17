package com.mayank.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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
    Task mGetTask;
    ImageButton back;
    TextView title;
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
        title.setText("ITEMS");
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



                                  //  Log.e("DAta", document.getData().get("Name").toString());
                                     // Log.i("Mayank",itemList.get(index).getSKU());
                                    Log.i("Indexoutside", ""+i);
                                    mGetTask= storageReference.child("ItemImages/" + document.getData().get("SKU").toString()).getDownloadUrl();
                                    storageReference.child("ItemImages/" + document.getData().get("SKU").toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            itemList.add(new ItemModel(document.getData().get("Name").toString(), document.getData().get("Type").toString(),document.getData().get("SKU").toString(), uri.toString(),
                                                    (long) document.getData().get("Cost"),(long)document.getData().get("Price"),(long)document.getData().get("Count"),document.getData().get("VendorId").toString()
                                            ,document.getData().get("Date").toString(),document.getData().get("Tax").toString()));
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


                            progressDialog.dismiss();
                        }
                      }   catch (Exception e){
                            e.printStackTrace();
                            Log.w("tag", "Error getting documents.", task.getException());
                           }
                    }
                });



    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void initListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ItemView.this, AddUpdateItems.class);
                startActivity(intent);
                ItemView.this.finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemView.this.finish();
            }
        });


    }

    private void initData() {
        db=FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(this);
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        index=0;
        title=findViewById(R.id.tvTitle);
        itemViewRecyclerViewAdapter= new ItemViewRecyclerViewAdapter(ItemView.this,itemList);


    }

    private void initViews() {
        floatingActionButton=findViewById(R.id.f1);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        toolbar =findViewById(R.id.generalToolbar);
        back=findViewById(R.id.imgBack);
    }
}
