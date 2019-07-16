package com.mayank.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddVendor extends AppCompatActivity {
    String vendorId;
    String vendorName;
    String vendorAddress;
    EditText etId;
    EditText etName;
    Toolbar toolbar;
    EditText etAddress;
    ImageButton back;
    TextView title;
    Button add;
    Vendor vendor;
    Button cancel;
    Map<String,Object> map=new HashMap();
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor);
        initView();
        initData();
        initListeners();
        setSupportActionBar(toolbar);

    }

    private void initListeners() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vendorId=etId.getText().toString();
                vendorName=etName.getText().toString();
                vendorAddress=etAddress.getText().toString();
                vendor=new Vendor(vendorName,vendorId,vendorAddress);

                if (TextUtils.isEmpty(vendorName) || vendorName == null || TextUtils.isEmpty(vendorId) || vendorId == null || TextUtils.isEmpty(vendorAddress)
                        || vendorAddress == null) {
                    Toast.makeText(AddVendor.this, "One or More Fields Are Empty", Toast.LENGTH_SHORT).show();
                    return;
                } else
                {   map.put("Address",vendor.getAddress());
                    map.put("Name",vendor.getName());
                    map.put("Id",vendor.getId());
                    db.collection("Vendors").document(vendorId)
                            .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("Mayank","vendor added");
                            Toast.makeText(AddVendor.this,"Add Vendor Successful",Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("Mayank","vendor addition failed");
                            Toast.makeText(AddVendor.this,"vendor addition failed",Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();

                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddVendor.this.finish();
            }
        });

    }

    private void initData() {
        add.setText("Add");
        cancel.setText("Cancel");
        title.setText("Add Vendor");
        db=FirebaseFirestore.getInstance();
    }

    private void initView() {
        etId=findViewById(R.id.etVId);
        etName=findViewById(R.id.etVname);
        etAddress=findViewById(R.id.etVaddress);
        title=findViewById(R.id.tvTitle);
        back=findViewById(R.id.imgBack);
        add=findViewById(R.id.btAdd);
        toolbar=findViewById(R.id.generalToolbar);
        cancel=findViewById(R.id.btCancel);

    }
}
