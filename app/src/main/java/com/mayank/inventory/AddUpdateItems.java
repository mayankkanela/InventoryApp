package com.mayank.inventory;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;

public class AddUpdateItems extends AppCompatActivity {
    Button addUpdate;
    Button cancel;
    Button takeImage;
    Button assignImage;
    private IntentIntegrator qrScan;
    FirebaseFirestore db;
    ItemModel itemModel;
    EditText name;
    EditText type;
    EditText sku;
    EditText price;
    EditText cost;
    String sName;
    String sType;
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
         flag=new Boolean(false);
         if(getIntent().hasExtra("UPDATE_FLAG")) {
             flag = getIntent().getExtras().getBoolean("UPDATE_FLAG");
             itemModel=getIntent().getExtras().getParcelable("ItemToUpdate");
         }

         initView();
         if(flag!=true)
         initData();
         else
             initData2();
            initListener();

    }

    private void initData2() {

        name.setText(itemModel.getName());
        sku.setText(itemModel.getSKU());
        sku.setEnabled(false);
        type.setText(itemModel.getType());
        type.setEnabled(false);
        price.setText(itemModel.getPrice().toString());
        cost.setText(itemModel.getCost().toString());
        addUpdate.setText("Update");
        progressDialog =new ProgressDialog(AddUpdateItems.this);
        cancel.setText("Cancel");
        db=FirebaseFirestore.getInstance();
         collectionReference=db.collection("Items");
        intent=new Intent(AddUpdateItems.this,ItemView.class);


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
                                    sType = type.getText().toString();
                                    sSku = sku.getText().toString();
                                    sCost = Long.parseLong(cost.getText().toString());
                                    sPrice = Long.parseLong(price.getText().toString());
                                }catch (Exception e)
                                {e.printStackTrace();}
                                if(TextUtils.isEmpty(sName)||TextUtils.isEmpty(sType)||TextUtils.isEmpty(sSku)||sCost==0
                                ||sPrice==0||uri==null)
                                {
                                    Toast.makeText(AddUpdateItems.this,"One or More Field are Empty",Toast.LENGTH_LONG).show();

                                }
                                else{
                                    item=new Item(sName,sType,sSku,sCost,sPrice,uri);
                                    progressDialog.setMessage("Uploading data");
                                    progressDialog.show();
                                    if(!flag){
                                    item.putItem();
                                    Toast.makeText(AddUpdateItems.this,"Item Add Successful",Toast.LENGTH_SHORT).show();
                                   }

                                    else {
                                        item.updateItem();
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
    }

    private void initData() {

        addUpdate.setText("Add");
        cancel.setText("Cancel");
        intent=new Intent(AddUpdateItems.this,ItemView.class);
        progressDialog =new ProgressDialog(AddUpdateItems.this);

    }

    private void initView() {
    name=findViewById(R.id.etName);
    type=findViewById(R.id.etType);
    sku=findViewById(R.id.etSKU);
    cost=findViewById(R.id.etCost);
    price=findViewById(R.id.etPrice);
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
              Log.e("karman", getRealPathFromURI(uri));
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