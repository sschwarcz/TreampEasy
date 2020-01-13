package com.schwarcz.samuel.trempeasy.lookingTremp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.schwarcz.samuel.trempeasy.MyTremp.CustomList;
import com.schwarcz.samuel.trempeasy.MyTremp.TrempsIRegisteredActivity;
import com.schwarcz.samuel.trempeasy.R;
import com.schwarcz.samuel.trempeasy.api.TrempHelper;
import com.schwarcz.samuel.trempeasy.base.BaseActivity;
import com.schwarcz.samuel.trempeasy.controller.MainAppActivity;
import com.schwarcz.samuel.trempeasy.model.SearchTremp;
import com.schwarcz.samuel.trempeasy.model.Tremp;

import java.io.Serializable;
import java.util.Vector;

import androidx.annotation.NonNull;

public class LookingTrempResultActivity extends BaseActivity implements Serializable {

    private DatabaseReference dbRef;
    private String uid ;
    private ListView mListview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_my_tremp);

        final SearchTremp st = (SearchTremp) getIntent().getSerializableExtra("SEARCH_TREMP");



        if(isCurrentUserLogged()) {
            uid = this.getCurrentUser().getUid();
            dbRef = FirebaseDatabase.getInstance().getReference().getRef().child("ListTremp");
            dbRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  //  int count = (int) dataSnapshot.getChildrenCount();
                    final Vector<DataSnapshot> tremps2 = new Vector<>();

                    for (DataSnapshot uid : dataSnapshot.getChildren()) {
                        for(DataSnapshot tremp : uid.getChildren()) {
                            if (tremp.exists()) {
                                Tremp t = tremp. getValue(Tremp.class);
                                String from = t.getFrom();
                                String to = t.getTo();
                                String hour = t.getHour();
                                String places = t.getPlaces();
                                if(from.equals(st.getFrom()) && to.equals(st.getTo()) && isEnoughPlaces(st.getPlaces(),t.getRemainPlaces())&& isHourCorrect(st.getHour_start(),st.getHour_end(),hour)){
                                    tremps2.add(tremp);
                                }
                            }
                        }
                    }
                    CustomList listAdapter = new CustomList(LookingTrempResultActivity.this, tremps2 , R.drawable.car2);

                    ListView list;

                    list = (ListView) findViewById(R.id.list);
                    list.setAdapter(listAdapter);
                    if(list.getCount()!=0) {
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    final int position, long id) {
                                Toast.makeText(LookingTrempResultActivity.this, "You Clicked at " + tremps2.get(+position), Toast.LENGTH_SHORT).show();

                                final String driverUID = getUID(tremps2.get(+position).getValue().toString());
                                Tremp u = tremps2.get(+position).getValue(Tremp.class);
                                u.setRemainPlaces(st.getPlaces());
                                final Tremp t = u;
                                //FirebaseDatabase.getInstance().getReference().child("ListRegister").child(uid).child(tremps2.get(+position).getKey()).setValue(t);
                                //TrempHelper.joinTremp(uid,driverUID,tremps2.get(+position).getKey(),Integer.parseInt(st.getPlaces()));
                                new AlertDialog.Builder(LookingTrempResultActivity.this)
                                        .setTitle("join the Tremp")
                                        .setMessage("Are you sure you want to join this tremp?")

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                FirebaseDatabase.getInstance().getReference().child("ListRegister").child(uid).child(tremps2.get(+position).getKey()).setValue(t);
                                                String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                                                TrempHelper.joinTremp(uid,name, driverUID, tremps2.get(+position).getKey(), Integer.parseInt(st.getPlaces()));
                                                Intent see_registered_tremp = new Intent(LookingTrempResultActivity.this, TrempsIRegisteredActivity.class);
                                                startActivity(see_registered_tremp);
                                            }
                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton(android.R.string.no, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();

                            }
                        });
                    }
                    else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(LookingTrempResultActivity.this);
                        builder1.setTitle("Not found");
                        builder1.setMessage("Try to check with other expectations");
                        builder1.setCancelable(true);
                        builder1.setNeutralButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Intent other_expectation_intent = new Intent(LookingTrempResultActivity.this, MainAppActivity.class);
                                        startActivity(other_expectation_intent);
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
                public boolean isEnoughPlaces(String wanted , String remain){

                    int w = Integer.parseInt(wanted);
                    int r = Integer.parseInt(remain);

                    return (w<=r);

                }
                public boolean isHourCorrect(String wantedStart , String wantedEnd , String hourOfTheTremp  ){

                    String[] start = wantedStart.split(":");
                    String[] end = wantedEnd.split(":");
                    String[] actual = hourOfTheTremp.split(":");
                    int start_hour = Integer.parseInt(start[0]);
                    int start_minute = Integer.parseInt(start[1]);
                    int end_hour = Integer.parseInt(end[0]);
                    int end_minute = Integer.parseInt(end[1]);
                    int act_hour = Integer.parseInt(actual[0]);
                    int act_minute = Integer.parseInt(actual[1]);

                    if(act_hour>=start_hour && act_minute>=start_minute &&act_hour<=end_hour && act_minute<=end_minute){
                        return true;
                    }else {
                        return false;
                    }
                }
                public  String getUID (String getvalue){

                    String [] split = getvalue.split("=|,|\\{");

                    return split[2];
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }






}
