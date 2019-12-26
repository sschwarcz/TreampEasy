
package com.schwarcz.samuel.trempeasy.auth;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.schwarcz.samuel.trempeasy.R;
import com.schwarcz.samuel.trempeasy.base.BaseActivity;
import com.schwarcz.samuel.trempeasy.controller.MainActivity;

public class ProfileActivity extends BaseActivity {

   private Button mLogoutBtn;
   private TextInputEditText mFname,mfullname,mTel,mMail;
   private ProgressBar mProfProdgressBar;

   private DocumentReference docref;
   private DatabaseReference dbRef;
    private String uid = this.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mProfProdgressBar=(ProgressBar)findViewById(R.id.progressbar_profile);
        mLogoutBtn = (Button) findViewById(R.id.logout_button_profile);
        mFname = (TextInputEditText)findViewById(R.id.first_name_profile);
        mfullname = (TextInputEditText)findViewById(R.id.full_name_profile);
        mTel = (TextInputEditText)findViewById((R.id.telephone_profile));
        mMail = (TextInputEditText) findViewById(R.id.mail_profile);
        mProfProdgressBar.setVisibility(View.INVISIBLE);

        mFname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isCurrentUserLogged()){
                    dbRef.child("username").setValue(s.toString());
                    docref.update("username",s.toString());
                }
            }
        });
        mTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isCurrentUserLogged()){
                    docref.update("telephone",s.toString());
                    dbRef.child("telephone").setValue(s.toString());
                }
            }
        });

        mLogoutBtn =(Button) findViewById(R.id.logout_button_profile);

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        updateUIWhenCreating();
        updateUIDatabase();
    }
    private void updateUIDatabase(){
        if(isCurrentUserLogged()) {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            dbRef = db.getReference("Users");
            dbRef = dbRef.child(uid);

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                   // String value = dataSnapshot.getValue(String.class);
                    //Log.d("test", "Value is: " + value);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("test", "Failed to read value.", databaseError.toException());
                }
            });
        }

    }


    private void updateUIWhenCreating(){

        if (this.getCurrentUser() != null){
            docref = FirebaseFirestore.getInstance().collection("Users").document(uid);

            //Get email & username from Firebase

            docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                       String email = documentSnapshot.getString("email");
                       String tel= documentSnapshot.getString("telephone");
                       String username= documentSnapshot.getString("username");
                       String fullName = documentSnapshot.getString("fullName");
                       mFname.setText(username);
                       mMail.setText(email);
                       mTel.setText(tel);
                       mfullname.setText(fullName);


                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        }
        else {
            Log.d("test", "not conected");
        }
    }

    private void logOut(){
     if(getCurrentUser() !=null){
         AuthUI.getInstance().signOut(this).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 Intent logout = new Intent(ProfileActivity.this, MainActivity.class);

                 startActivity(logout);
             }
         });
     }

    }

}

