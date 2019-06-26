package com.mayank.inventory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class LoginActivity extends AppCompatActivity {
    private String email;
     EditText editText1 ;
     EditText editText2 ;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        editText1 = findViewById(R.id.etUserName);
        editText2 =findViewById(R.id.etPassword);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
         View view=findViewById(R.id.login_Activity);
        overrideFonts(this,view);
        ImageButton button = findViewById(R.id.btLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               clickFunction();
            }
        });
        TextView textView=findViewById(R.id.forgotPassword);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //   Toast.makeText(LoginActivity.this,"Forgot",Toast.LENGTH_LONG).show();
                final AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Forgot Password");
                builder.setMessage("Enter your registered email");
                final EditText input=new EditText(LoginActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        email =input.getText().toString();
                        FirebaseFirestore db =FirebaseFirestore.getInstance();
                        CollectionReference collectionReference =db.collection("Users");
                       collectionReference.whereEqualTo("Email",email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                           @Override
                           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                               if (!queryDocumentSnapshots.getDocuments().isEmpty()){
                                   Toast.makeText(LoginActivity.this,"Password :"+queryDocumentSnapshots.getDocuments().get(0).getString("Password"),Toast.LENGTH_LONG).show();
                               } else {
                                   Toast.makeText(LoginActivity.this,"Incorrect Email",Toast.LENGTH_LONG).show();
                               }

                           }
                       })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(LoginActivity.this,"Unable to Connect",Toast.LENGTH_LONG);
                           }
                       });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();

            }
        });
        textView = (TextView) findViewById(R.id.tvSignIn);

        TextPaint paint = textView.getPaint();
        float width = paint.measureText("Sign In");

        Shader textShader = new LinearGradient(0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#7652f9"),
                        Color.parseColor("#9b5df8"),
                }, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);

    }
public void clickFunction()
{
    final String s1 =editText1.getText().toString();
    final String s2 =editText2.getText().toString();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
   final int[] i = {0};
    db.collection("Users")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful())
               {
                   for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                   {
                       String s3= documentSnapshot.getData().get("UserId").toString();
                       String s4= documentSnapshot.getData().get("Password").toString();
                         Log.i("server :",documentSnapshot.getData().get("UserId").toString());
                         Log.i("tag",s1);
                         Log.i("tag",s2);
                       Log.e("tag",s3);
                       Log.e("tag",s4);

                       if(s1.equalsIgnoreCase(s3) && s2.equalsIgnoreCase(s4)){
                           sharedPref=getSharedPreferences("LOGIN_FLAG_FILE", 0);
                           editor= sharedPref.edit();
                           editor.putInt("LOGIN_FLAG",1);
                           editor.commit();
                           i[0]=1;
                           Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                           startActivity(intent);
                          finish();
                           Log.e("User", "Login Sucessful");

                       }


                   }
                   if(i[0]==0)
                       Toast.makeText(LoginActivity.this,"Incorrect Email or Password",Toast.LENGTH_LONG).show();


                }
            }
            });

}
    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView ) {
                 if(v.getId()!=R.id.tvSignIn)
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Quicksand_Regular.otf"));
                 else
                     ((TextView)v).setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Quicksand_Bold.otf"));
            }
        } catch (Exception e) {
        }
    }
}
