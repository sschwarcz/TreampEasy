package com.schwarcz.samuel.trempeasy.lookingTremp;

import androidx.annotation.NonNull;


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
import com.schwarcz.samuel.trempeasy.base.BaseActivity;
import com.schwarcz.samuel.trempeasy.model.SearchTremp;

import java.io.Serializable;

public class LookingTrempActivity extends BaseActivity implements AdapterView.OnItemSelectedListener , Serializable {
    Button mSearchBtn;
    Spinner mFrom , mTo , mHours_start , mMinutes_start,mHours_end , mMinutes_end , mPlaces;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looking_tremp);

        mFrom = (Spinner)findViewById(R.id.looking_for_spinner_from);
        mTo =(Spinner) findViewById(R.id.looking_for_spinner_to);
        mHours_start =(Spinner) findViewById(R.id.looking_for_spinner_hours_start);
        mMinutes_start =(Spinner) findViewById(R.id.looking_for_spinner_minutes_start);
        mHours_end =(Spinner) findViewById(R.id.looking_for_spinner_hours_end);
        mMinutes_end =(Spinner) findViewById(R.id.looking_for_spinner_minutes_end);
        mPlaces =(Spinner) findViewById(R.id.looking_for_spinner_places);
        mSearchBtn =(Button) findViewById(R.id.looking_for_search_button);

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
        initSpinners();

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                look_for_tremps();
            }
        });



    }

    private void initSpinners(){


        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.douze, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mHours_start.setAdapter(arrayAdapter);
        mHours_start.setOnItemSelectedListener(this);
        mHours_end.setAdapter(arrayAdapter);
        mHours_end.setOnItemSelectedListener(this);
        mPlaces.setAdapter(arrayAdapter);
        mPlaces.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> arrayAdapter2 = ArrayAdapter.createFromResource(this,R.array.soixente, android.R.layout.simple_spinner_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMinutes_start.setAdapter(arrayAdapter2);
        mMinutes_start.setOnItemSelectedListener(this);
        mMinutes_end.setAdapter(arrayAdapter2);
        mMinutes_end.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> arrayAdapter3 = ArrayAdapter.createFromResource(this,R.array.cities, android.R.layout.simple_spinner_item);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFrom.setAdapter(arrayAdapter3);
        mFrom.setOnItemSelectedListener(this);
        mTo.setAdapter(arrayAdapter3);
        mTo.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void look_for_tremps(){

        if (this.getCurrentUser() != null){
            String uid = this.getCurrentUser().getUid();
            String from = mFrom.getSelectedItem().toString();
            String to = mTo.getSelectedItem().toString();
            String exact_hour_start = mHours_start.getSelectedItem().toString() +":"+mMinutes_start.getSelectedItem().toString();
            String exact_hour_end = mHours_end.getSelectedItem().toString() +":"+mMinutes_end.getSelectedItem().toString();
            String places = mPlaces.getSelectedItem().toString();
            SearchTremp st = new SearchTremp(uid,from,to,exact_hour_start,exact_hour_end,places);
            Intent looking_for_result_intent = new Intent(LookingTrempActivity.this,LookingTrempResultActivity.class);
            looking_for_result_intent.putExtra("SEARCH_TREMP",st);
            startActivity(looking_for_result_intent);

        }
    }
}
