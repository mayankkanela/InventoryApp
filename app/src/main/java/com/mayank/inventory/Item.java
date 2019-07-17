package com.mayank.inventory;

import androidx.annotation.NonNull;

import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.HashMap;
import java.util.Map;

public class Item {
    String name;
    String type;
    String sku;
    Long cost;
    Long price;
    Uri uri;
    long count;
    String date;
    String VendorId;
    String tax;
    FirebaseFirestore db;
    Map<String,Object> item= new HashMap<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVendorId() {
        return VendorId;
    }

    public void setVendorId(String vendorId) {
        VendorId = vendorId;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }




    public Item() {

        name = new String();
        type = new String();
        sku = new String();
        cost = new Long(0);
        price = new Long(0);
        count=new Long(0);
    }

    public Item(String sName, String sType, String sSku, Long sCost, Long sPrice,Uri uri) {
        name=sName;
        type=sType;
        sku=sSku;
        cost=sCost;
        price=sPrice;
        this.uri=uri;

    }
    public Item(String sName, String sType, String sSku, Long sCost, Long sPrice,Uri uri,String vendorId,String date,String tax) {
        name=sName;
        type=sType;
        sku=sSku;
        cost=sCost;
        price=sPrice;
        this.uri=uri;
        this.VendorId=vendorId;
        this.date=date;
        this.tax=tax;

    }


    public void putItem()
    {

        item.put("SKU",sku);
        item.put("Name",name);
        item.put("Type",type);
        item.put("Price",price);
        item.put("Cost",cost);
        item.put("Count",0);
        db=FirebaseFirestore.getInstance();
        db.collection("Items").document(sku)
                  .set(item)
                  .addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {

                          Log.i("tag:","Document Snapshot Successfully Written");
                        }
                  })
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Log.i("tag:","Addition failed");
                      }
                  });

        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();
        StorageReference imageref=storageReference.child("ItemImages").child(sku);
        UploadTask uploadTask=imageref.putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Log.i("Mayank","Upload successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Mayank","Upload Failed");
            }
        });

     }
  public  void updateItem()
  {
      //item.put("SKU",sku);
      item.put("Name",name);
      item.put("Type",type);
      item.put("Price",price);
      item.put("Cost",cost);
      item.put("Status",1);
      db=FirebaseFirestore.getInstance();
      db.collection("Items").document(sku)
              .update(item)
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                     Log.i("Tag:","Document updated");
                     db.collection("Stock").whereEqualTo("SKU",sku)
                             .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                         @Override
                         public void onComplete(@NonNull Task<QuerySnapshot> task) {
                             if(task.isSuccessful())
                             {
                                 item.clear();
                                 item.put("Name",name);
                                 item.put("Type",type);
                                 if (!task.getResult().isEmpty())
                                 {
                                for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
                                {
                                    DocumentReference documentReference=queryDocumentSnapshot.getReference();
                                    documentReference.update(item);

                                }

                                 }
                             }
                         }
                     });

                   }
               })
              .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Log.i("Tag:","Document Update failed");
                  }
              });
      FirebaseStorage storage=FirebaseStorage.getInstance();
      StorageReference storageReference=storage.getReference();
      StorageReference imageref=storageReference.child("ItemImages").child(sku);
      UploadTask uploadTask=imageref.putFile(uri);



  }
  public void deleteItem()
  {   FirebaseFirestore db2;
  db2=FirebaseFirestore.getInstance();
      db=FirebaseFirestore.getInstance();
     db.collection("Items").document(sku)
             .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
         @Override
         public void onSuccess(Void aVoid) {
         Log.i("Mayank","Delete Success");

         }
     }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
        Log.i("Mayank","Delete Failed");
         }
     });
      db2.collection("Stock").whereEqualTo("SKU",sku)
              .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
              if(!task.getResult().isEmpty())
                  for (QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
                  { DocumentReference documentReference=queryDocumentSnapshot.getReference();
                      documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                              Log.i("Mayank","stock deleted");
                          }
                      }).addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              Log.i("Mayank","Stock deleted");
                          }
                      });
                  }
          }

      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
              Log.i("Mayank","Stock Retrieval failed");

          }
      });
      FirebaseStorage storage=FirebaseStorage.getInstance();
      StorageReference storageReference=storage.getReference();
      StorageReference imageref=storageReference.child("ItemImages").child(sku);
      Task task=imageref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            Log.i("Mayank","Image Deleted");
          }
      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
              Log.i("Mayank","Image not deleted");

          }
      });


  }
  public  void putStock(String barcode, final String sku,String name,String type)
  {
      item.put("Barcode",barcode);
      item.put("SKU",sku);
      item.put("Name",name);
      item.put("Type",type);
      db=FirebaseFirestore.getInstance();
      db.collection
              ("Stock").document(barcode)
              .set(item)
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                      getCount(sku);
                      updateCount(sku);
                      Log.i("tag:","Document Snapshot Successfully Written");
                  }
              })
              .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Log.i("tag:","Addition failed");
                  }
              });


  }
  public  void updateStock()
  {
      item.clear();
      item.put("Name",name);
      item.put("Type",type);
      db=FirebaseFirestore.getInstance();
      db.collection
              ("Stock").whereEqualTo("SKU",sku).get()
              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      if(task.isSuccessful())
                          if(!task.getResult().isEmpty())
                          {
                              for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
                              {
                                  queryDocumentSnapshot.getReference().update(item);
                              }
                          }
                  }
              }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
           Log.i("Update Stock:","Failed");
          }
      });


  }
  public void getType()
  {
      db=FirebaseFirestore.getInstance();
      db.collection("Items")
              .get()
              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      if(task.isSuccessful())
                          for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
                          {
                              type=queryDocumentSnapshot.getData().get("Type").toString();
                          }
                  }
              });


  }
  void getCount(String sku)
  {
      db=FirebaseFirestore.getInstance();
      CollectionReference collectionReference=db.collection("Items");
      collectionReference.whereEqualTo("SKU",sku).get()
              .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                  Log.i("Mayank:","Unable to find sku");
                  }
              })
              .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                  @Override
                  public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                      count =queryDocumentSnapshots.getDocuments().get(0).getLong("Count");
                      Log.i("Mayank","count : "+count);
                  }
              });



  }
  void updateCount(String sku)
  {
      db=FirebaseFirestore.getInstance();
      CollectionReference collectionReference=db.collection("Items");
      collectionReference.whereEqualTo("SKU",sku).get()
              .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                  @Override
                  public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                      DocumentReference documentReference=queryDocumentSnapshots.getDocuments().get(0).getReference();
                      documentReference.update("Count",count+1);
                  }
              });

  }


}
