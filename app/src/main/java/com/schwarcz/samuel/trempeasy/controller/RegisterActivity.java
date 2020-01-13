package com.schwarcz.samuel.trempeasy.controller;


import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.schwarcz.samuel.trempeasy.R;
import com.schwarcz.samuel.trempeasy.api.UserHelper;
import com.schwarcz.samuel.trempeasy.base.BaseActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static android.content.ContentValues.TAG;


public class RegisterActivity extends BaseActivity {

    private EditText mUsername ,mFullname, mEmail , mPassword,mPhoneNumber;
    private Button mSubmit;
    private boolean mEnabled_name=false ,mEnabled_mail=false , mEnabled_password=false ,mEnabled_phone=false;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //firebase

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    //redirect


                } else {
                    // User is signed out
                    Log.d("test", "No USer Conected yet");

                }

            }
        };
        //firebase;


        mUsername = (EditText)findViewById(R.id.register_activity_username_input);
        mFullname=(EditText)findViewById(R.id.register_activity_fullname_input);
        mEmail = (EditText)findViewById(R.id.register_activity_email_input);
        mPassword = (EditText)findViewById(R.id.register_activity_password_input);
        mPhoneNumber = (EditText)findViewById(R.id.register_activity_phone_input);
        mSubmit = (Button) findViewById(R.id.register_activity_submit_button);


        mSubmit.setEnabled(mEnabled_mail && mEnabled_name && mEnabled_password && mEnabled_phone);


        mUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() != 0){
                    mEnabled_name = true;

                }else{
                    mEnabled_name = false;
                }
                mSubmit.setEnabled(mEnabled_mail && mEnabled_name && mEnabled_password && mEnabled_phone);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mFullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() != 0){
                    mEnabled_mail=true;

                }else{

                    mEnabled_mail=false;
                }
                mSubmit.setEnabled(mEnabled_mail && mEnabled_name && mEnabled_password && mEnabled_phone);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() != 0){
                    mEnabled_password=true;

                }else{
                    mEnabled_password =false;
                }
                mSubmit.setEnabled(mEnabled_mail && mEnabled_name && mEnabled_password && mEnabled_phone);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() != 0){
                    mEnabled_phone=true;

                }else{
                    mEnabled_phone =false;
                }
                mSubmit.setEnabled(mEnabled_mail && mEnabled_name && mEnabled_password && mEnabled_phone);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register(mEmail.getText().toString(), mPassword.getText().toString());

            }
        });






    }

    private void register(String email , String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        Log.d("test", " Verification : signIn With Email:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user.
                        if (!task.isSuccessful()) {
                            Log.d("test","not success");
                        }
                        else{
                            createUserInFirestore();

                            Intent mainApp_Intent = new Intent(RegisterActivity.this , MainAppActivity.class);
                            startActivity(mainApp_Intent);
                        }
                    }
                });
    }
    private void createUserInFirestore(){

        if (this.getCurrentUser() != null){



           // String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String username =mUsername.getText().toString();
            String uid = this.getCurrentUser().getUid();
            String mail = mEmail.getText().toString();
            String tel = mPhoneNumber.getText().toString();
            String full = mFullname.getText().toString();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username+",")

                    .build();
            FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);



            UserHelper.createUser(uid, username,full ,mail , tel).addOnFailureListener(this.onFailureListener());
        }
    }




}
