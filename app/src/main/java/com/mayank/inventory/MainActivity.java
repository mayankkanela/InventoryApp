package com.mayank.inventory;

import android.content.Intent;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    //FloatingActionButton b1;
   /*
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> bcode=new ArrayList<>();
    ArrayList<String> descp=new ArrayList<>();*/
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
        final Button Item =findViewById(R.id.Item);
        Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,ItemView.class);
                startActivity(intent);
                finish();
            }
        });
        Button Vendor=findViewById(R.id.Vendor);
        Vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Vendor Pressed",Toast.LENGTH_LONG).show();
            }
        });
        Button AddStock =findViewById(R.id.Add_stock);
        AddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Stock Check Pressed",Toast.LENGTH_LONG).show();
            }
        });
        Button StockCheck =findViewById(R.id.Stock_Check);
        StockCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Stock Check Preesed",Toast.LENGTH_LONG).show();
            }
        });

       // b1 = (FloatingActionButton) findViewById(R.id.f1);
      //  qrScan = new IntentIntegrator(this);
        Toolbar toolbar =findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        // Access a Cloud Firestore instance from your Activity
     /*   b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });*/
        /*
*/
       ImageButton button =findViewById(R.id.logOut);
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

    }

 /*   @Override
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

    }*/


}
