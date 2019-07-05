package com.mayank.inventory;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;

import java.io.ByteArrayOutputStream;

public class AddInventory extends AppCompatActivity {
    Button addUpdate;
    Button cancel;
    Button takeImage;
    Button assignImage;
    private IntentIntegrator qrScan;
    FirebaseFirestore db;
    EditText name;
    EditText type;
    EditText sku;
    EditText price;
    EditText cost;
    String sName;
    String sType;
    String sSku;
    String sPrice;
    String sCost;
    Item item;
    Uri uri;
    ImageView itemPic;
    Intent intent;
    EditText imageUrl;
    private static final int CAMERA_REQUEST_CODE=1001;
    ProgressDialog progressDialoge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_inventory);
        initView();
        initData();
        initListener();

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
                                sName=name.getText().toString();
                                sType=type.getText().toString();
                                sSku=sku.getText().toString();
                                sCost=cost.getText().toString();
                                sPrice=price.getText().toString();
                                if(TextUtils.isEmpty(sName)||TextUtils.isEmpty(sType)||TextUtils.isEmpty(sSku)||TextUtils.isEmpty(sCost)
                                ||TextUtils.isEmpty(sPrice))
                                {
                                    Toast.makeText(AddInventory.this,"One or More Field are Empty",Toast.LENGTH_LONG).show();
                                }
                                else{
                                    item=new Item(sName,sType,sSku,sCost,sPrice,uri);
                                    progressDialoge.setMessage("Uploading data");
                                    progressDialoge.show();
                                    item.putItem();
                                    progressDialoge.dismiss();
                                    Toast.makeText(AddInventory.this,"Item Add Successful",Toast.LENGTH_SHORT).show();
                                    }
                                startActivity(intent);
                                finish();
                            }
                        });
                        takeImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(ContextCompat.checkSelfPermission(AddInventory.this,Manifest.permission.CAMERA)!=
                                        PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(AddInventory.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                                        PackageManager.PERMISSION_GRANTED){
                                    ActivityCompat.requestPermissions(AddInventory.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            CAMERA_REQUEST_CODE);

                                }
                                else
                                { Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                 startActivityForResult(intent,CAMERA_REQUEST_CODE);

                                }
                            }
                        });
    }

    private void initData() {

        addUpdate.setText("Add");
        cancel.setText("Cancel");
        intent=new Intent(AddInventory.this,ItemView.class);
        progressDialoge=new ProgressDialog(AddInventory.this);
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