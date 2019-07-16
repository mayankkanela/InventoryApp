package com.mayank.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class VendorView extends AppCompatActivity {
    ArrayList<Vendor> vendorArrayList=new ArrayList<>();
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ImageButton delete;
    TextView title;
    ImageButton back;
    FirebaseFirestore db;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_view);
        initView();
        initData();
        setSupportActionBar(toolbar);
        initListener();
    }

    private void initListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(VendorView.this,AddVendor.class);
                startActivity(intent);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VendorView.this.finish();
            }
        });

    }

    private void initData() {
           // toolbar.setTitle("Vendors");
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            db=FirebaseFirestore.getInstance();
            builder=new AlertDialog.Builder(this);
            builder.setTitle("Attention");
            title.setText("VENDORS");
            builder.setMessage("Do You Really Want to Delete Vendor");

            db.collection("Vendors")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {     int i=0;
                                for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
                                {
                                    vendorArrayList.add(new Vendor(queryDocumentSnapshot.getData().get("Name").toString(),queryDocumentSnapshot.
                                    getData().get("Id").toString(),queryDocumentSnapshot.getData().get("Address").toString()));
                                    i++;
                                }
                                recyclerView.setAdapter(new RecyclerViewAdapterVendor(vendorArrayList));
                            }
                        }
                    });


    }

    private void initView() {
        floatingActionButton=findViewById(R.id.floatingActionButtonVendor);
        recyclerView=findViewById(R.id.recyclerViewVendor);
        toolbar=findViewById(R.id.generalToolbarVendor);
        title=findViewById(R.id.tvTitle);
        back=findViewById(R.id.imgBack);
    }
public class RecyclerViewAdapterVendor extends RecyclerView.Adapter<RecyclerViewAdapterVendor.recyclerViewHolder>{
    private ArrayList<Vendor> vendors;

    public RecyclerViewAdapterVendor(ArrayList<Vendor> vendors) {
        this.vendors = vendors;
    }

    @NonNull
    @Override
    public recyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.list_item_view_vendor,parent,false);
        return new recyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewHolder holder, int position) {
        if(vendors.get(position).getId()!=null){
         holder.name.setText(vendors.get(position).getName());
         holder.id.setText(vendors.get(position).getId());
         holder.address.setText(vendors.get(position).getAddress());
            }
    }

    @Override
    public int getItemCount() {
        return vendors.size();
    }
 public class recyclerViewHolder extends RecyclerView.ViewHolder {
     TextView name;
     TextView id;
     TextView address;
     public recyclerViewHolder(@NonNull View itemView) {
         super(itemView);
         name=itemView.findViewById(R.id.tvVendrName);
         id=itemView.findViewById(R.id.tvVendorID);
         address=itemView.findViewById(R.id.tvVendorAddr);
         delete=itemView.findViewById(R.id.btDelete2);
         delete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(final DialogInterface dialogInterface, int i) {
                         db.collection("vendor_pic").document(vendors.get(getAdapterPosition()).getId())
                                 .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {
                                 Log.i("Mayank","vendor deleted");
                                 Intent intent=new Intent(VendorView.this,VendorView.class);
                                 startActivity(intent);
                                 finish();


                             }
                         }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Log.i("Mayank","vendor not deleted");
                             }
                         });
                         dialogInterface.dismiss();
                     }
                 }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         dialogInterface.dismiss();
                         return;
                     }
                 });
             builder.create();
             builder.show();
             }
         });
     }
 }
}
}
