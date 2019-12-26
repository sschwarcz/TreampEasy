package com.schwarcz.samuel.trempeasy.MyTremp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.schwarcz.samuel.trempeasy.R;
import com.schwarcz.samuel.trempeasy.base.BaseActivity;
import com.schwarcz.samuel.trempeasy.controller.MainAppActivity;

import java.util.Vector;

public class TrempsIRegisteredActivity extends BaseActivity {
    private DatabaseReference dbRef;
    private String uid ;
    private ListView mListview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_my_tremp);


        if(isCurrentUserLogged()) {
            uid = this.getCurrentUser().getUid();

            dbRef = FirebaseDatabase.getInstance().getReference().getRef().child("ListRegister").child(uid);
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count = (int) dataSnapshot.getChildrenCount();
                    final Vector<DataSnapshot> tremps2 = new Vector<>();
                    Log.d("test", String.valueOf(count));
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Log.d("test", child.getKey());
                        tremps2.add(child);


                    }
                    CustomList listAdapter = new CustomList(TrempsIRegisteredActivity.this, tremps2);

                    ListView list;
                    list = (ListView) findViewById(R.id.list);
                    list.setAdapter(listAdapter);
                    if(list.getCount()!=0){
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Toast.makeText(TrempsIRegisteredActivity.this, "You Clicked at " + tremps2.get(+position), Toast.LENGTH_SHORT).show();
                        }
                    });}else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(TrempsIRegisteredActivity.this);
                        builder1.setTitle("Not found");
                        builder1.setMessage("you didn't register any tremp yet");
                        builder1.setCancelable(true);
                        builder1.setNeutralButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Intent other_expectation_intent = new Intent(TrempsIRegisteredActivity.this, MainAppActivity.class);
                                        startActivity(other_expectation_intent);
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }
}
