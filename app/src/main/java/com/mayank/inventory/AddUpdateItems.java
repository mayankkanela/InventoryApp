package com.mayank.inventory;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddUpdateItems extends AppCompatActivity {
    Button addUpdate;
    Button cancel;
    Button takeImage;
    Button assignImage;
    private IntentIntegrator qrScan;
    FirebaseFirestore db;
    ItemModel itemModel;
    EditText name;
    EditText sku;
    EditText price;
    EditText cost;
    EditText vId;
    EditText tax;
    EditText date;
    String sName;
    ArrayList<String> types=new ArrayList<>();
    Spinner spinner;
    Button addType;
    String sType;
    String sDate;
    String sTax;
    String sVId;
    String sSku;
    Long sPrice;
    Long sCost;
    Intent intent2;
    Item item;
    Uri uri;
    Boolean flag;
    String docname;
    CollectionReference  collectionReference;
    ImageView itemPic;
    Intent intent;
    private static final int CAMERA_REQUEST_CODE=1001;
    private static final int PICK_IMAGE_CODE=1002;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_inventory);
        initView();
        initData();
        initListener();
        initSpinner();
        if(flag)
        {
            Glide.with(AddUpdateItems.this).load(uri).into(itemPic);
        }

    }

    private void initSpinner() {
        types.add("Select type");
        db = FirebaseFirestore.getInstance();
        db.collection("Type")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                                    types.add(queryDocumentSnapshot.getData().get("Key").toString());
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddUpdateItems.this, android.R.layout
                                        .simple_spinner_item, types) {
                                    @Override
                                    public boolean isEnabled(int position) {
                                        if (position == 0)
                                            return false;
                                        else
                                            return true;
                                    }

                                    @Override
                                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                        View view = super.getDropDownView(position, convertView, parent);
                                        TextView tv = (TextView) view;
                                        tv.setTextColor(Color.BLACK);
                                        if (position == 0)
                                            tv.setTextColor(Color.GRAY);


                                        return view;
                                    }
                                };

                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(arrayAdapter);
                                if (flag) {
                                    int i = 0;
                                    for (String type : types) {
                                        if (type.equalsIgnoreCase(itemModel.getType())) {
                                            spinner.setSelection(i);
                                            break;
                                        }
                                        i++;
                                    }
                                }

                            }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void initListener() {
    cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(intent);
            finish();

        }
    });
    addUpdate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                sName = name.getText().toString();
                sSku = sku.getText().toString();
                sCost = Long.parseLong(cost.getText().toString());
                sPrice = Long.parseLong(price.getText().toString());
                sVId=vId.getText().toString();
                sTax=tax.getText().toString();
                sDate=date.getText().toString();
            }catch (Exception e)
            {e.printStackTrace();}
            if(TextUtils.isEmpty(sName)||TextUtils.isEmpty(sType)||TextUtils.isEmpty(sSku)|| sCost==0
            ||sPrice==0||uri==null||TextUtils.isEmpty(sVId)||TextUtils.isEmpty(sTax)||TextUtils.isEmpty(sDate)
            ||sDate.length()!=9)
            {
                Toast.makeText(AddUpdateItems.this,"One or More Field are Empty",Toast.LENGTH_LONG).show();

            }
            else{
                item=new Item(sName,sType,sSku,sCost,sPrice,uri,sVId,sTax,sDate);
                progressDialog.setMessage("Uploading data");
                progressDialog.show();
                if(!flag){
                item.putItem();
                Toast.makeText(AddUpdateItems.this,"Item Add Successful",Toast.LENGTH_SHORT).show();
               }

                else {
                    item.updateItem();
                    item.updateStock();
                Toast.makeText(AddUpdateItems.this,"Item Update Successful",Toast.LENGTH_SHORT).show();
               }
                progressDialog.dismiss();
                startActivity(intent);
                AddUpdateItems.this.finish();

                 }


        }
    });
    takeImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(ContextCompat.checkSelfPermission(AddUpdateItems.this,Manifest.permission.CAMERA)!=
                    PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(AddUpdateItems.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(AddUpdateItems.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        CAMERA_REQUEST_CODE);

            }
            else
            { Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
             startActivityForResult(intent,CAMERA_REQUEST_CODE);

            }
        }
    });
      assignImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent();
              intent.setType("image/*");
              intent.setAction(Intent.ACTION_GET_CONTENT);
              startActivityForResult(intent,PICK_IMAGE_CODE);
          }
      });
      spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              if(i>0)
              {
                  sType=adapterView.getItemAtPosition(i).toString();
              }
          }

          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {

          }

      });
        addType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(AddUpdateItems.this);
                builder.setTitle("Enter Type");
                final EditText editText=new EditText(AddUpdateItems.this);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(editText);
                builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        CollectionReference collectionReference=db.collection("Type");
                        collectionReference.whereEqualTo("Type",editText.getFreezesText()).get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot querySnapshot) {
                                        if(querySnapshot.isEmpty()) {
                                            Map<String, Object> temp = new HashMap<>();
                                            temp.put("Key", editText.getText().toString());
                                            db.collection("Type").document(editText.getText().toString())
                                                    .set(temp)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.i("Mayank", "Add type complete");
                                                            initSpinner();
                                                            dialogInterface.dismiss();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.i("Mayank", "Add type failed");
                                                }
                                            });
                                        }
                                        else
                                            Toast.makeText(AddUpdateItems.this,"Type Already Present",Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("Mayank","Fetch Failed");
                                dialogInterface.dismiss();
                            }
                        });

                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.create();
                builder.show();

            }
        });
    }

    private void initData() {
        flag=new Boolean(false);
        if(getIntent().hasExtra("UPDATE_FLAG")) {
            flag = getIntent().getExtras().getBoolean("UPDATE_FLAG");
            itemModel=getIntent().getExtras().getParcelable("ItemToUpdate");
        }
        if(flag!=true) {
            addUpdate.setText("Add");
            cancel.setText("Cancel");
            intent = new Intent(AddUpdateItems.this, ItemView.class);
            progressDialog = new ProgressDialog(AddUpdateItems.this);
        }else{
            name.setText(itemModel.getName());
            sku.setText(itemModel.getSKU());
            sku.setEnabled(false);
            price.setText(itemModel.getPrice().toString());
            cost.setText(itemModel.getCost().toString());
            date.setText(itemModel.getDate());
            tax.setText(itemModel.getTax());
            vId.setText(itemModel.getvId());
            addUpdate.setText("Update");
            progressDialog =new ProgressDialog(AddUpdateItems.this);
            cancel.setText("Cancel");
            db=FirebaseFirestore.getInstance();
            collectionReference=db.collection("Items");
            intent=new Intent(AddUpdateItems.this,ItemView.class);
            uri=Uri.parse(itemModel.getImageUrl());
        }

    }

    private void initView() {
        name=findViewById(R.id.etName);
        sku=findViewById(R.id.etSKU);
        spinner=findViewById(R.id.spinner);
        addType=findViewById(R.id.addType);
        cost=findViewById(R.id.etCost);
        price=findViewById(R.id.etPrice);
        date=findViewById(R.id.etDate);
        tax=findViewById(R.id.etTax);
        vId=findViewById(R.id.etVId);
        addUpdate=findViewById(R.id.addUpdate);
        cancel =findViewById(R.id.Cancel);
        takeImage=findViewById(R.id.btTakeImage);
        assignImage=findViewById(R.id.btAssignImage);
        itemPic=findViewById(R.id.itemImageAdi);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(requestCode==CAMERA_REQUEST_CODE&&resultCode==RESULT_OK)
         {
             Bitmap photo = (Bitmap) data.getExtras().get("data");
             //imageView.setImageBitmap(photo);

             // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
              uri = getImageUri(getApplicationContext(), photo);
              itemPic.setImageBitmap(photo);
              Log.e("test", getRealPathFromURI(uri));
         }
       if(requestCode==PICK_IMAGE_CODE&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
       {
           uri=data.getData();
           itemPic.setImageURI(uri);
       }
    }

  /*  public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }*/
        //to get full resolution
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

}