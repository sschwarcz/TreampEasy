package com.schwarcz.samuel.trempeasy.controller;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.schwarcz.samuel.trempeasy.MyTremp.SeeMyTrempActivity;
import com.schwarcz.samuel.trempeasy.MyTremp.TrempsIRegisteredActivity;
import com.schwarcz.samuel.trempeasy.R;
import com.schwarcz.samuel.trempeasy.auth.ProfileActivity;
import com.schwarcz.samuel.trempeasy.MyTremp.CreateTrempActivity;
import com.schwarcz.samuel.trempeasy.base.BaseActivity;
import com.schwarcz.samuel.trempeasy.lookingTremp.LookingTrempActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MainAppActivity extends BaseActivity {

    private TextView mNameUser;
    private Button  mLookingBtn,mSeeMyTrempsBtn , mPlanningBtn , mRegisteredTrempButton;
    private FloatingActionButton mfloatingBtn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        mfloatingBtn = (FloatingActionButton) findViewById(R.id.mainapp_activity_floatingActionButton);
        mLookingBtn = (Button) findViewById(R.id.mainapp_activity_looking_button);
        mPlanningBtn = (Button) findViewById(R.id.mainapp_activity_planning_button);
        mNameUser = (TextView) findViewById(R.id.mainapp_activity_hey_name_text);
        mSeeMyTrempsBtn= (Button) findViewById(R.id.mainapp_activity_my_tremps_button);
        mRegisteredTrempButton = (Button) findViewById(R.id.mainapp_activity_my_the_tremp_i_register);
        updateUI();

        mfloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile_Intent = new Intent(MainAppActivity.this , ProfileActivity.class);
                startActivity(profile_Intent);
            }
        });
        mLookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent looking_tremp_Intent = new Intent(MainAppActivity.this , LookingTrempActivity.class);
                startActivity(looking_tremp_Intent);
            }
        });
        mPlanningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent create_tremp_Intent = new Intent(MainAppActivity.this , CreateTrempActivity.class);
                startActivity(create_tremp_Intent);
            }
        });

        mSeeMyTrempsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent see_tremp_Intent = new Intent(MainAppActivity.this , SeeMyTrempActivity.class);
                startActivity(see_tremp_Intent);
            }
        });
        mRegisteredTrempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent see_registered_tremp = new Intent(MainAppActivity.this , TrempsIRegisteredActivity.class);
                startActivity(see_registered_tremp);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();


    }

    private void updateUI(){
        if(isCurrentUserLogged()){
            String uid = this.getCurrentUser().getUid();
            DocumentReference docref = FirebaseFirestore.getInstance().collection("Users").document(uid);
            docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        String username= documentSnapshot.getString("username");
                        mNameUser.setText("Hey "+username+" !");

                    }
                }
            });
        }

    }
}
