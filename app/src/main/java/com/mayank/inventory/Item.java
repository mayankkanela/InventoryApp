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
    String cost;
    String price;
    Uri uri;
    int status;
    long count;
    FirebaseFirestore db;
    Map<String,Object> item= new HashMap<>();

    public Item() {

        name = new String();
        type = new String();
        sku = new String();
        cost = new String();
        price = new String();
    }

    public Item(String sName, String sType, String sSku, String sCost, String sPrice,Uri uri) {
        name=sName;
        type=sType;
        sku=sSku;
        cost=sCost;
        price=sPrice;
        this.uri=uri;

    }

    public void putItem()
    {

        item.put("SKU",sku);
        item.put("Name",name);
        item.put("Type",type);
        item.put("Price",price);
        item.put("Cost",cost);
        item.put("Count",0);
        item.put("Status",1);
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
        final CollectionReference collectionReference=db.collection("Type");
        collectionReference.whereEqualTo("Type",type).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty())
                        {   Map<String,Object> temp=new HashMap<>();
                            temp.put("Key",type);
                            db=FirebaseFirestore.getInstance();
                            db.collection("Type").document(type)
                                    .set(temp)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("Mayank","Add type complete");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i("Mayank","Add type failed");
                                }
                            });
                        }

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
  public  void updateItem(String docName)
  {
      item.put("SKU",sku);
      item.put("Name",name);
      item.put("Type",type);
      item.put("Price",price);
      item.put("Cost",cost);
      item.put("Status",1);
      db=FirebaseFirestore.getInstance();
      db.collection("Items").document(docName)
              .update(item)
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                     Log.i("Tag:","Document updated");
                   }
               })
              .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Log.i("Tag:","Document Update failed");
                  }
              });


  }
  public void deleteItem(String docName)
  {
      db=FirebaseFirestore.getInstance();
    DocumentReference item1= db.collection("Items").document(docName);
              item1.update("Status",0)
                      .addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                              Log.i("Tag","Item Deleted");
                          }
                      })
                      .addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              Log.i("Tag:","Deletion Failed");
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
      db.collection("Stock").document()
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

/*public  Item(@NonNull EditText editText, @NonNull EditText editText2, String string)
{
    barcode =string;
    name=editText.getText().toString();
    descp=editText2.getText().toString();

}*/

}
