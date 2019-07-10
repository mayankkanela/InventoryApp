package com.mayank.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ExpandedItemView extends AppCompatActivity {
    private Intent intent;
    private ItemModel itemModel;
    private Button update;
    private Button delete;
    private TextView tvName;
    private TextView tvSku;
    private TextView tvType;
    private TextView tvPrice;
    private TextView tvCost;
    private Item item;
    private TextView tvTotalCount;
    private ImageView pic;
    TextView temp;
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
        if(itemModel.getImageUrl()!=null|| !TextUtils.isEmpty(itemModel.getImageUrl()))
        Glide.with(this).load(itemModel.getImageUrl()).into(pic);


        Log.e("karman", "sku : " +itemModel.getSKU());

        db.collection("Stock")
         .whereEqualTo("SKU",itemModel.getType())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                            try {
                                if (!task.getResult().isEmpty()) {
                                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                        temp.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT));
                                        temp.setText(queryDocumentSnapshot.getData().get("Barcode").toString());
                                        ExpandedItemView.this.linearLayout.addView(temp);
                                    }
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

    private void initListener() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ExpandedItemView.this, AddUpdateItems.class);
                intent.putExtra("UPDATE_FLAG",true);
                intent.putExtra("ItemToUpdate",itemModel);
                startActivity(intent);


            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogbuilder.create();
                alertDialogbuilder.show();

            }
        });

     }

    private void initData() {
    intent=getIntent();
        itemModel=intent.getExtras().getParcelable("Item");
        try {
            tvName.setText(itemModel.getName());
            tvType.setText(itemModel.getSKU());
            tvSku.setText(itemModel.getType());
            tvTotalCount.setText(itemModel.getCount().toString());
            tvPrice.setText(itemModel.getPrice().toString());
            tvCost.setText(itemModel.getCost().toString());
        }catch (Exception e)
        {e.printStackTrace();}


        db=FirebaseFirestore.getInstance();
        alertDialogbuilder=new AlertDialog.Builder(ExpandedItemView.this);
        alertDialogbuilder.setTitle("Attention! Deleting Item will completely delete the Item and its Stocks");
        alertDialogbuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {
                CollectionReference collectionReference=db.collection("Items");
                collectionReference.whereEqualTo("SKU",itemModel.getSKU())
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String docName;
                        docName=queryDocumentSnapshots.getDocuments().toString();
                        item.deleteItem(docName);
                        intent.setClass(ExpandedItemView.this,ItemView.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                   Log.i("Mayank","Error failed to delete!");
                    }
                });

            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

}

    private void initViews() {
     tvName=findViewById(R.id.tvName);
     tvSku=findViewById(R.id.tvSKU);
     tvType=findViewById(R.id.tvType);
     tvCost=findViewById(R.id.tvCost);
     tvPrice=findViewById(R.id.tvPrice);
     tvTotalCount=findViewById(R.id.tvCount);
     delete=findViewById(R.id.btDelete);
     update=findViewById(R.id.btUpdate);
     pic=findViewById(R.id.imgItem);
     item=new Item();
     linearLayout=findViewById(R.id.linearLayout);
    }
}
