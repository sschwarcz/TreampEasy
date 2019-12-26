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
import com.schwarcz.samuel.trempeasy.R;
import com.schwarcz.samuel.trempeasy.base.BaseActivity;
import androidx.annotation.NonNull;



public class SignInActivity extends BaseActivity {

    private EditText mEmailText, mPasswordText;
    private Button mNextButton;
    private boolean mEnabled_button_MAIL = false;
    private boolean mEnabled_button_PASS = false;
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
        setContentView(R.layout.activity_sign_in);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d("test", user.getDisplayName()+"conected");
                    Intent mainApp_Intent = new Intent(SignInActivity.this , MainAppActivity.class);
                    startActivity(mainApp_Intent);

                } else {
                    // User is signed out
                    Log.d("test", "no user conected");

                }

            }
        };
        ///firebase

        mEmailText = (EditText) findViewById(R.id.signin_activity_email_input);
        mPasswordText= (EditText) findViewById(R.id.signin_activity_password_input);
        mNextButton = (Button) findViewById(R.id.signIn_next_button);



        mNextButton.setEnabled(mEnabled_button_MAIL && mEnabled_button_PASS);


        mEmailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(s.toString().length() !=0 ) {
                   mEnabled_button_MAIL = true;
               } else{
                   mEnabled_button_MAIL = false;
               }

                mNextButton.setEnabled(mEnabled_button_MAIL && mEnabled_button_PASS);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() !=0 ) {
                    mEnabled_button_PASS = true;
                } else{
                    mEnabled_button_PASS = false;
                }
                mNextButton.setEnabled(mEnabled_button_MAIL && mEnabled_button_PASS);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mAuth.signOut();
                signIn(mEmailText.getText().toString(),mPasswordText.getText().toString());

            }
        });
    }

    protected void signIn(String mail, String password){
        Log.d("test","signIn Function enter");
        mAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                Log.d("test", " Verification : signIn With Email:onComplete:" + task.isSuccessful());
                //  If sign in succeeds i.e if task.isSuccessful(); returns true then the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.


                // If sign in fails, display a message to the user.
                if (!task.isSuccessful()) {
                    Log.d("test","not success");
                }
                else{
                    Intent mainApp_Intent = new Intent(SignInActivity.this , MainAppActivity.class);
                    startActivity(mainApp_Intent);
                }
            }
        });
    }


}
