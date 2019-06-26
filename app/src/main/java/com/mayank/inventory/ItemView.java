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
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.Collections;

public class ItemView extends AppCompatActivity {
    FloatingActionButton b1;
    private IntentIntegrator qrScan;
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> bcode=new ArrayList<>();
    ArrayList<String> descp=new ArrayList<>();
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        Toolbar toolbar =findViewById(R.id.generalToolbar);
        setSupportActionBar(toolbar);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db=FirebaseFirestore.getInstance();
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
                                    name.add(i,document.getData().get("Name").toString());
                                    bcode.add(i,document.getData().get("Barcode").toString());
                                    descp.add(i,document.getData().get("Description").toString());
                                    i++;


                                }
                            }
                            Collections.sort(name, Collections.reverseOrder());
                            Collections.sort(bcode, Collections.reverseOrder());
                            Collections.sort(descp, Collections.reverseOrder());
                            recyclerView.setAdapter(new RecyclerViewAdapter(bcode,name,descp));
                        } else {
                            Log.w("tag", "Error getting documents.", task.getException());
                        }
                    }
                });
        FloatingActionButton floatingActionButton=findViewById(R.id.f1);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ItemView.this,AddInventory.class);
                startActivity(intent);
                finish();

            }
        });

    }
}
