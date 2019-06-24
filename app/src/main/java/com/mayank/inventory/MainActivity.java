package com.mayank.inventory;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {
    FloatingActionButton b1;
    private IntentIntegrator qrScan;
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> bcode=new ArrayList<>();
    ArrayList<String> descp=new ArrayList<>();
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = getSharedPreferences("LOGIN_FLAG_FILE", 0);
        int loginFlag = sharedPref.getInt("LOGIN_FLAG", 0); //0 is the default value

        Log.e("test", ""+loginFlag);
        if(loginFlag==0)
        {   Intent intent =new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        b1 = (FloatingActionButton) findViewById(R.id.f1);
        qrScan = new IntentIntegrator(this);
        Toolbar toolbar =findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button button =findViewById(R.id.logOut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this, LoginActivity.class);
                editor = sharedPref.edit();
                editor.putInt("LOGIN_FLAG",0);
                editor.commit();
                startActivity(intent);
                finish();
            }
        });
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result not found", Toast.LENGTH_LONG).show();
            } else {

                // Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
                String s1 = (String) result.getContents();

                Intent intent = new Intent(this, AddInventory.class);
                Bundle bundle = new Bundle();
                bundle.putString("Barcode", s1);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }

    }


}
