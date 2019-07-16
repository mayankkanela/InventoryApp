package com.mayank.inventory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.FallbackServiceBroker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class StockCheck extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView noDataShow;
    TextView title;
    Button startScan;
    String bcode;
    Boolean flag1;
    Integer pos;
    Toolbar toolbar;
    ImageButton imageButton;
    IntentIntegrator qrScan;
    FirebaseFirestore db;
    CollectionReference collectionReference;
    ArrayList<String> bcodes=new ArrayList<>();
    ArrayList<ItemModel> itemModels=new ArrayList<>();
    StockCheckAdapter stockCheckAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_check);
        initViews();
        initData();
        initListeners();
        setSupportActionBar(toolbar);

    }

    private void initListeners() {
        startScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StockCheck.this.finish();
            }
        });

    }

    private void initData() {
        qrScan=new IntentIntegrator(this);
        qrScan.setBeepEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db=FirebaseFirestore.getInstance();
        pos=0;
        collectionReference=db.collection("Stock");
        stockCheckAdapter=new StockCheckAdapter(itemModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(stockCheckAdapter);
        flag1=false;
        title.setText("STOCK CHECK");
    }

    private void initViews() {
        toolbar=findViewById(R.id.generalToolbar);
        title=findViewById(R.id.tvTitle);
       startScan=findViewById(R.id.startScan);
       recyclerView=findViewById(R.id.StockCheckRecyclerView);
       noDataShow=findViewById(R.id.noData);
       imageButton=findViewById(R.id.imgBack);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult!=null)
        {
            if(intentResult.getContents()==null)
                Toast.makeText(this,"No Result Found",Toast.LENGTH_SHORT).show();
            else {
                bcode = intentResult.getContents();
                for(int i=0;i<bcodes.size();i++)
                    if(bcode.equalsIgnoreCase(bcodes.get(i))) {
                        Toast.makeText(this, "Already Scanned", Toast.LENGTH_SHORT).show();
                        return;
                    }
                addStockCount();
            }
        }
    }

    private void addStockCount() {
        collectionReference.whereEqualTo("Barcode",bcode).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty())
                        {
                           for(int i=0;i<pos;i++)
                           { if(itemModels.get(i).getSKU().equalsIgnoreCase(queryDocumentSnapshots.getDocuments().get(0).getString("SKU")))
                           {
                               flag1=true;
                               itemModels.get(i).setCount(itemModels.get(i).getCount()+1);
                               stockCheckAdapter.notifyDataSetChanged();
                               break;
                           }
                           }
                         if(!flag1){
                             itemModels.add(pos,new ItemModel(queryDocumentSnapshots.getDocuments().get(0).getString("Name"),
                                     queryDocumentSnapshots.getDocuments().get(0).getString("Type"),queryDocumentSnapshots.getDocuments()
                             .get(0).getString("SKU"),""));
                             itemModels.get(pos).setCount(itemModels.get(pos).getCount()+1);
                             stockCheckAdapter.notifyDataSetChanged();
                             pos++;
                         }
                         bcodes.add(bcode);
                         noDataShow.setVisibility(View.GONE);

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Mayank","Unable to fetch");

            }
        });
     }
}
