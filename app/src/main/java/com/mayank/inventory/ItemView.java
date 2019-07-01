package com.mayank.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class ItemView extends AppCompatActivity {


    FirebaseFirestore db;
    Toolbar toolbar;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    ArrayList<ItemModel>itemList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        initViews();
        initData();
        initListener();
        toolbar.setTitle("Items");
        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        db.collection("Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("Status").toString().equalsIgnoreCase("1"))
                                {

                                  //  Log.e("DAta", document.getData().get("Name").toString());
                                    itemList.add(new ItemModel(document.getData().get("Name").toString(), document.getData().get("Type").toString(),document.getData().get("SKU").toString()));



                                }
                            }
                            /*Collections.sort(name, Collections.reverseOrder());
                            Collections.sort(bcode, Collections.reverseOrder());
                            Collections.sort(descp, Collections.reverseOrder());*/
                         //   Collections.sort(itemList, Collections.reverseOrder());
                           /* ArrayList<ItemModel>tempData = new ArrayList<ItemModel>(itemList);*/
                            Collections.sort(itemList);
                            recyclerView.setAdapter(new ItemViewRecyclerViewAdapter(itemList));
                        } else {
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


    }

    private void initViews() {
        floatingActionButton=findViewById(R.id.f1);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        toolbar =findViewById(R.id.generalToolbar);
    }
}
