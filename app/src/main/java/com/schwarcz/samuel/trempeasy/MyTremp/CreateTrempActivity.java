package com.schwarcz.samuel.trempeasy.MyTremp;

import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.schwarcz.samuel.trempeasy.R;
import com.schwarcz.samuel.trempeasy.api.TrempHelper;
import com.schwarcz.samuel.trempeasy.base.BaseActivity;
import com.schwarcz.samuel.trempeasy.controller.MainAppActivity;

public class CreateTrempActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    Button mSubmitBtn;
    Spinner mFrom , mTo , mHours , mMinutes , mPlaces;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tremp);

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

        mFrom = (Spinner)findViewById(R.id.create_tremp_spinner_from);
        mTo =(Spinner) findViewById(R.id.create_tremp_spinner_to);
        mHours =(Spinner) findViewById(R.id.create_tremp_spinner_hours);
        mMinutes =(Spinner) findViewById(R.id.create_tremp_spinner_minutes);
        mPlaces =(Spinner) findViewById(R.id.create_tremp_spinner_places);
        mSubmitBtn =(Button) findViewById(R.id.submit_create_tremp);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.douze, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mHours.setAdapter(arrayAdapter);
        mHours.setOnItemSelectedListener(this);
        mPlaces.setAdapter(arrayAdapter);
        mPlaces.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> arrayAdapter2 = ArrayAdapter.createFromResource(this,R.array.soixente, android.R.layout.simple_spinner_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMinutes.setAdapter(arrayAdapter2);
        mMinutes.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> arrayAdapter3 = ArrayAdapter.createFromResource(this,R.array.cities, android.R.layout.simple_spinner_item);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFrom.setAdapter(arrayAdapter3);
        mFrom.setOnItemSelectedListener(this);
        mTo.setAdapter(arrayAdapter3);
        mTo.setOnItemSelectedListener(this);



        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTrempOnFirestore();
                getDialog();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void createTrempOnFirestore(){
        if (this.getCurrentUser() != null){
            String uid = this.getCurrentUser().getUid();
            String from = mFrom.getSelectedItem().toString();
            String to = mTo.getSelectedItem().toString();
            String exact_hour = mHours.getSelectedItem().toString() +":"+mMinutes.getSelectedItem().toString();
            String places = mPlaces.getSelectedItem().toString();
            TrempHelper.createTremp(uid , from ,to , exact_hour,places);
        }
    }
    private void getDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(CreateTrempActivity.this);
        builder1.setTitle("Success");
        builder1.setMessage("you succefully created a tremp");
        builder1.setCancelable(true);
        builder1.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent other_expectation_intent = new Intent(CreateTrempActivity.this, MainAppActivity.class);
                        startActivity(other_expectation_intent);
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


}
