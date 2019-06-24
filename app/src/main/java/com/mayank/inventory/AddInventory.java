package com.mayank.inventory;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AddInventory extends AppCompatActivity {
    Button button ;
    Button button1;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
     String docName;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_inventory);
        Intent intent = getIntent();
        Bundle bundle =intent.getExtras();
        final String bcode = bundle.getString("Barcode");
        Toolbar toolbar=findViewById(R.id.generalToolbar);
        toolbar.setTitle(bcode);
        setSupportActionBar(toolbar);
        final String[] name1 = new String[1];
        final String[] descp1 = new String[1];
        final EditText name = findViewById(R.id.etName);
         final EditText descp = findViewById(R.id.etDescription);
         button = findViewById(R.id.addUpdate);
         button1=findViewById(R.id.deleteItem);
        db.collection("Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                            {
                                if(documentSnapshot.getData().get("Barcode").toString().equalsIgnoreCase(bcode)&& documentSnapshot.getData().get("Status").toString().equalsIgnoreCase("1"))
                                {
                                    name.setText(documentSnapshot.getData().get("Name").toString());
                                    descp.setText(documentSnapshot.getData().get("Description").toString());
                                    name1[0] =name.getText().toString();
                                    descp1[0] =descp.getText().toString();
                                    docName= documentSnapshot.getId();
                                }

                            }
                            if (docName==null) {
                                button1.setText("CANCEL");
                                button.setText("ADD");
                            }
                                else {
                                button1.setText("DELETE");
                                button.setText("UPDATE");
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddInventory.this,"Check network connection",Toast.LENGTH_LONG).show();
                    }
                });





         final Intent intent1 =new Intent(this,MainActivity.class);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item=new Item(name,descp,bcode);
                  if(docName==null){
                item.putItem();
                  }
                     else
                    if(name1[0].equalsIgnoreCase(name.getText().toString())||descp1[0].equalsIgnoreCase(descp.getText().toString()))
                    item.updateItem(docName);

                startActivity(intent1);
                finish();
            }
        });

      button1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if(docName!=null) {
                  Item item = new Item(name, descp, bcode);
                  item.deleteItem(docName);
              }

                  startActivity(intent1);
                  finish();

          }
      });

    }



}
