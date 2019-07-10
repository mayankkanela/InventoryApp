package com.mayank.inventory;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.preference.DialogPreference;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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


public class MainActivity extends AppCompatActivity{

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Button Item;
    Button AddStock;
    Button Vendor;
    Button StockCheck;
    ImageButton button;
    Toolbar toolbar;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        title.setText("MAIN MENU");
        initListener();


    }

    private void initData() {
        sharedPref = getSharedPreferences("LOGIN_FLAG_FILE", 0);
        int loginFlag = sharedPref.getInt("LOGIN_FLAG", 0); //0 is the default value
        Log.e("test", ""+loginFlag);
        if(loginFlag==0)
        {   Intent intent =new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void initListener() {

        Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,ItemView.class);
                startActivity(intent);
            }
        });

        Vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,VendorView.class);
                startActivity(intent);
            }
        });
        AddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             Intent intent=new Intent(MainActivity.this,AddStock.class);
             startActivity(intent);
            }
        });

        StockCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, StockCheck.class);
                startActivity(intent);
            }
        });


        setSupportActionBar(toolbar);

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

    private void initView() {
        Item =findViewById(R.id.Item);
        AddStock =findViewById(R.id.Add_stock);
        Vendor=findViewById(R.id.Vendor);
        button =findViewById(R.id.logOut);
        StockCheck =findViewById(R.id.Stock_Check);
        toolbar =findViewById(R.id.mainToolbar);
        title=findViewById(R.id.tvTitleMain);

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

                Intent intent = new Intent(this, AddUpdateItems.class);
                Bundle bundle = new Bundle();
                bundle.putString("Barcode", s1);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }

    }*/


}
