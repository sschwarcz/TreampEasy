package com.schwarcz.samuel.trempeasy.controller;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import com.schwarcz.samuel.trempeasy.R;
import com.schwarcz.samuel.trempeasy.MyTremp.CreateTrempActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button mSignIn,mRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSignIn = (Button) findViewById(R.id.main_activity_signin_button);
        mRegister= (Button) findViewById(R.id.main_activity_register_button);

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn_Intent = new Intent(MainActivity.this , SignInActivity.class);
                startActivity(signIn_Intent);
            }
        });
//
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register_Intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register_Intent);
            }
        });

    }


}
