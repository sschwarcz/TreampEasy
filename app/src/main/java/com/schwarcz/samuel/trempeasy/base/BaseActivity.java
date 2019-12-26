package com.schwarcz.samuel.trempeasy.base;

import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {


    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); } protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"on FAillurListener", Toast.LENGTH_LONG).show();
            }
        };
    }

}
