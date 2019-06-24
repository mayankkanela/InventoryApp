package com.mayank.inventory;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private String barcode,descp,name;
    int status;
    FirebaseFirestore db;
    Map<String,Object> item= new HashMap<>();
    public void putItem()
    {

        item.put("Barcode",barcode);
        item.put("Name",name);
        item.put("Description",descp);
        item.put("Status",1);
        db=FirebaseFirestore.getInstance();
        db.collection("Items").document()
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
     }
  public  void updateItem(String docName)
  {
      item.put("Barcode",barcode);
      item.put("Name",name);
      item.put("Description",descp);
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
                      Log.i("Tag:","Document Updation failed");
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
public  Item(@NonNull EditText editText, @NonNull EditText editText2, String string)
{
    barcode =string;
    name=editText.getText().toString();
    descp=editText2.getText().toString();

}

}
