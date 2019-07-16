package com.mayank.inventory;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
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
    CardView Item;
    CardView AddStock;
    CardView Vendor;
    CardView StockCheck;
    ImageButton button;
    Toolbar toolbar;
    ConnectivityManager cm;
    boolean isConnected;
    NetworkInfo activeNetwork;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
        setSupportActionBar(toolbar);
        title.setText("MAIN MENU");

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
                cm=(ConnectivityManager)MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                activeNetwork=cm.getActiveNetworkInfo();
                isConnected=activeNetwork!=null&&
                        activeNetwork.isConnectedOrConnecting();
             if(isConnected)
             {  Intent intent= new Intent(MainActivity.this,ItemView.class);
                startActivity(intent);
             }
             else
                 Toast.makeText(MainActivity.this,"Please Check Internet Connection",Toast.LENGTH_SHORT).show();

            }


        });

        Vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cm=(ConnectivityManager)MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                activeNetwork=cm.getActiveNetworkInfo();
                isConnected=activeNetwork!=null&&
                        activeNetwork.isConnectedOrConnecting();
                if(isConnected)
                {Intent intent=new Intent(MainActivity.this,VendorView.class);
                startActivity(intent);
                }
                else
                    Toast.makeText(MainActivity.this,"Please Check Internet Connection",Toast.LENGTH_SHORT).show();
            }
        });
        AddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cm=(ConnectivityManager)MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                activeNetwork=cm.getActiveNetworkInfo();
                isConnected=activeNetwork!=null&&
                        activeNetwork.isConnectedOrConnecting();
                if(isConnected)
                {
             Intent intent=new Intent(MainActivity.this,AddStock.class);
             startActivity(intent);
                  }

                else{
                    Toast.makeText(MainActivity.this,"Please Check Internet Connection",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });
        StockCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cm=(ConnectivityManager)MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                activeNetwork=cm.getActiveNetworkInfo();
                isConnected=activeNetwork!=null&&
                        activeNetwork.isConnectedOrConnecting();
                if(isConnected)
                {
                Intent intent=new Intent(MainActivity.this, StockCheck.class);
                startActivity(intent);
            }

                else
                    Toast.makeText(MainActivity.this,"Please Check Internet Connection",Toast.LENGTH_SHORT).show();
            }

        });




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

}
