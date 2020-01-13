package com.schwarcz.samuel.trempeasy.MyTremp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SeeMyTrempActivity extends BaseActivity {
    
    private DatabaseReference dbRef;
    private  DatabaseReference dbTrempist;
    private String uid ;
    public static final int REQUEST_CALL = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CALL){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//
            }
            else{
                Toast.makeText(SeeMyTrempActivity.this, "permission denied" , Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_my_tremp);


        if(isCurrentUserLogged()) {
            uid = this.getCurrentUser().getUid();

            dbRef = FirebaseDatabase.getInstance().getReference().getRef().child("ListTremp").child(uid);
            dbTrempist =dbRef;
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Vector<DataSnapshot> tremps2 = new Vector<>();
                    final ArrayAdapter<String> trempistList = new ArrayAdapter<String>(SeeMyTrempActivity.this, android.R.layout.select_dialog_item);
                    final ArrayAdapter<String> trempistListuid = new ArrayAdapter<String>(SeeMyTrempActivity.this, android.R.layout.select_dialog_item);
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        tremps2.add(child);


                    }
                    CustomList listAdapter = new CustomList(SeeMyTrempActivity.this, tremps2,R.drawable.car);


                    /////////////////////////////////
                    ListView list;
                    list = (ListView) findViewById(R.id.list);
                    list.setAdapter(listAdapter);
                    if(list.getCount()!=0){
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Toast.makeText(SeeMyTrempActivity.this, "You Clicked at " + tremps2.get(+position), Toast.LENGTH_SHORT).show();

                            dbTrempist = dbTrempist.child(tremps2.get(+position).getKey()).child("traveller");
                            // TODO Auto-generated method stub
                            dbTrempist.addListenerForSingleValueEvent(new ValueEventListener() {
                                private String[] getInfos(String value){
                                   String[] rslt = new String[2];

                                    String [] split = value.split("[=,{]");

                                    return split;
                                }
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                       final String[] val = getInfos(child.getValue().toString());
                                        String uidTrempist =val[1];
                                        String usernameTrempist = val[2];
                                        trempistList.add(usernameTrempist);
                                        trempistListuid.add(uidTrempist);


                                    }
                                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SeeMyTrempActivity.this);
                                    alertBuilder.setIcon(R.drawable.car2);
                                    alertBuilder.setTitle("List of registered Trempist");


                                    alertBuilder.setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            });

                                    alertBuilder.setAdapter(trempistList, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,int which) {

                                                    dialog.dismiss();
                                                }
                                            });

                                    final AlertDialog alertDialog = alertBuilder.create();
                                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                                        @Override
                                        public void onShow(DialogInterface dialog) {
                                            // TODO Auto-generated method stub
                                            ListView listView = alertDialog.getListView();
                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Toast.makeText(SeeMyTrempActivity.this, "You Clicked at " + trempistList.getItem(+position), Toast.LENGTH_SHORT).show();

                                                    makePhoneCall(trempistList.getItem(+position),trempistListuid.getItem(+position));
                                                    alertDialog.dismiss();
                                                }

                                                private void makePhoneCall(String username , final String uidTrempist) {
                                                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                                                        ActivityCompat.requestPermissions(SeeMyTrempActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
                                                        makePhoneCall(username ,uidTrempist);

                                                    }
                                                    else{
                                                        new AlertDialog.Builder(SeeMyTrempActivity.this)
                                                                .setTitle("Call your trempist")
                                                                .setMessage("Are you sure you want to call "+username+" ?")
                                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        DatabaseReference refUser=    FirebaseDatabase.getInstance().getReference().getRef().child("Users").child(uidTrempist).child("telephone");
                                                                        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                String dial = "tel:"+dataSnapshot.getValue();
                                                                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });

                                                                    }
                                                                })

                                                                // A null listener allows the button to dismiss the dialog and take no further action.
                                                                .setNegativeButton(android.R.string.no, null)
                                                                .setIcon(android.R.drawable.ic_menu_call)
                                                                .show();

                                                        ///////////


                                                    }
                                                }

                                            });
                                        }
                                    });

                                    alertDialog.show();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
                    });
                    }else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SeeMyTrempActivity.this);
                    builder1.setTitle("Not found");
                    builder1.setMessage("you didn't create any tremp yet");
                    builder1.setCancelable(true);
                    builder1.setNeutralButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent other_expectation_intent = new Intent(SeeMyTrempActivity.this, MainAppActivity.class);
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
