package com.mayank.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ExpandedItemView extends AppCompatActivity {
    private Intent intent;
    private ItemModel itemModel;
    private Button update;
    private Button delete;
    private TextView tvName;
    private TextView tvVId;
    private TextView tvTax;
    private TextView tvDate;
    private TextView tvSku;
    private TextView tvType;
    private TextView tvPrice;
    private TextView tvCost;
    private Item item;
    ImageButton imageButton;
    Toolbar toolbar;
    private TextView tvTotalCount;
    private ImageView pic;
    TextView title;
    Uri uri;

    ArrayList<String> barcodes=new ArrayList<>();
   // ViewGroup.LayoutParams layoutParams;
    AlertDialog.Builder alertDialogbuilder;
    private FirebaseFirestore db;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expanded_item_view);
        initViews();
        initData();
        initListener();
        setSupportActionBar(toolbar);
        title.setText(itemModel.getType());
        if(itemModel.getImageUrl()!=null|| !TextUtils.isEmpty(itemModel.getImageUrl()))
        Glide.with(this).load(itemModel.getImageUrl()).into(pic);


        Log.e("Mayank", "sku : " +itemModel.getSKU());

        db.collection("Stock")
         .whereEqualTo("SKU",itemModel.getSKU())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                            try {
                                if (!task.getResult().isEmpty()) {
                                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                                           barcodes.add(queryDocumentSnapshot.getData().get("Barcode").toString());


                                    }
                                }

                                for(final String barcode: barcodes){
                                    View  view=getLayoutInflater().inflate(R.layout.stock_list,null);
                                    TextView  temp=view.findViewById(R.id.Barcode);
                                    ImageButton btdeleteStock =view.findViewById(R.id.btdeleteStock);
                                    temp.setText(barcode);
                                    btdeleteStock.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            stockDeleteFunction(barcode);
                                        }
                                    });
                                    Log.e("karman", "barcode " + barcode);
                                    ExpandedItemView.this.linearLayout.addView(view);

                                }




                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            Log.i("Mayank","Fetch Bcode Failed");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Mayank","Failed to get Barcode");
            }
        });
          }

    private void stockDeleteFunction(final String barcode) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Delete Stock");
        builder.setMessage("Do You Want To Delete Stock :"+barcode);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                db.collection("Stock").document(barcode).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("Mayank", "Delete Success");
                        intent.setClass(ExpandedItemView.this,ExpandedItemView.class);
                        startActivity(intent);
                        finish();
                        dialogInterface.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Mayank", "Delete Failed");
                    }
                });

            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create();
        builder.show();

    }
    private void initListener() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ExpandedItemView.this, AddUpdateItems.class);
                intent.putExtra("UPDATE_FLAG",true);
                intent.putExtra("ItemToUpdate",itemModel);
                startActivity(intent);
                ExpandedItemView.this.finish();


            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogbuilder.create();
                alertDialogbuilder.show();

            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


     }

    private void initData() {
    intent=getIntent();
        itemModel=intent.getExtras().getParcelable("Item");
        uri.parse(itemModel.getImageUrl());
        item=new Item(itemModel.getName(),itemModel.getType(),itemModel.getSKU(),itemModel.getCost(),itemModel.getPrice(),uri);
        try {
            tvName.setText("Name     :"+itemModel.getName());
            tvType.setText("Type      :"+itemModel.getType());
            tvSku.setText("SKU       :"+itemModel.getSKU());
            tvTotalCount.setText("Total     :"+itemModel.getCount().toString());
            tvPrice.setText("Cost      :"+itemModel.getPrice().toString());
            tvCost.setText("Price     :"+itemModel.getCost().toString());
            tvVId.setText("Vendor Id :"+itemModel.getvId());
            tvDate.setText("Date      :"+itemModel.getDate());
            tvTax.setText("Tax       :"+itemModel.getTax()+"%");
        }catch (Exception e)
        {e.printStackTrace();}


        db=FirebaseFirestore.getInstance();
        alertDialogbuilder=new AlertDialog.Builder(ExpandedItemView.this);
        alertDialogbuilder.setTitle("Attention!");
        alertDialogbuilder.setMessage("Deleting Item will completely delete the Item and its Stocks");
        alertDialogbuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {

                        item.deleteItem();
                        intent.setClass(ExpandedItemView.this,ItemView.class);
                        startActivity(intent);
                        finish();

            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

    //  layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    private void initViews() {
     tvName=findViewById(R.id.tvName);
     tvSku=findViewById(R.id.tvSKU);
     tvType=findViewById(R.id.tvType);
     tvCost=findViewById(R.id.tvCost);
     tvPrice=findViewById(R.id.tvPrice);
     tvTax=findViewById(R.id.tvTax);
     tvVId=findViewById(R.id.tvVid);
     tvDate=findViewById(R.id.tvDate);
     tvTotalCount=findViewById(R.id.tvCount);
     delete=findViewById(R.id.btDelete);
     update=findViewById(R.id.btUpdate);
     pic=findViewById(R.id.imgItem);
     linearLayout=findViewById(R.id.linearLayout);
     toolbar=findViewById(R.id.generalToolbar);
     title=findViewById(R.id.tvTitle);
     imageButton=findViewById(R.id.imgBack);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            intent.setClass(ExpandedItemView.this,ItemView.class);
          startActivity(intent);
    }
}
