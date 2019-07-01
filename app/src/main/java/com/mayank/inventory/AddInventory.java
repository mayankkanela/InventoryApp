package com.mayank.inventory;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;

public class AddInventory extends AppCompatActivity {
    Button button;
    Button button1;
    private IntentIntegrator qrScan;
    String bcode;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String docName;
    EditText barcode ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_inventory);
        Toolbar toolbar = findViewById(R.id.generalToolbar);
        toolbar.setTitle("Add New Item");
        setSupportActionBar(toolbar);
        final String[] name1 = new String[1];
        final String[] descp1 = new String[1];
        final EditText name = findViewById(R.id.etName);
        final EditText descp = findViewById(R.id.etType);
        button = findViewById(R.id.addUpdate);
        button.setText("ADD");
        button1 = findViewById(R.id.deleteItem);
        button1.setText("CANCEL");
        barcode=findViewById(R.id.etImage);
        /*db.collection("Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if (documentSnapshot.getData().get("Barcode").toString().equalsIgnoreCase(bcode) && documentSnapshot.getData().get("Status").toString().equalsIgnoreCase("1")) {
                                    name.setText(documentSnapshot.getData().get("Name").toString());
                                    descp.setText(documentSnapshot.getData().get("Description").toString());
                                    name1[0] = name.getText().toString();
                                    descp1[0] = descp.getText().toString();
                                    docName = documentSnapshot.getId();
                                }

                            }
                            if (docName == null) {
                                button1.setText("CANCEL");
                                button.setText("ADD");
                            } else {
                                button1.setText("DELETE");
                                button.setText("UPDATE");
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddInventory.this, "Check network connection", Toast.LENGTH_LONG).show();
                    }
                });*/


        Button Scan = findViewById(R.id.ScanBcode);
        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan = new IntentIntegrator(AddInventory.this);
                qrScan.initiateScan();


            }
        });


        final Intent intent1 = new Intent(this, MainActivity.class);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bcode=barcode.getText().toString();
                if(TextUtils.isEmpty(bcode) || TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(descp.getText()))
                {
                    Toast.makeText(AddInventory.this,"One or more fields are empty",Toast.LENGTH_LONG).show();
                    return;
                }
               else {
                    Item item = new Item(name, descp, bcode);
                    item.putItem();
                } /*else if (name1[0].equalsIgnoreCase(name.getText().toString()) || descp1[0].equalsIgnoreCase(descp.getText().toString()))
                    item.updateItem(docName);
                   */
                startActivity(intent1);
                finish();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (docName != null) {
                    Item item = new Item(name, descp, bcode);
                    item.deleteItem(docName);
                }*/

                startActivity(intent1);
                finish();

            }
        });

    }


}