package com.schwarcz.samuel.trempeasy.api;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.schwarcz.samuel.trempeasy.model.SearchTremp;
import com.schwarcz.samuel.trempeasy.model.Tremp;
import com.schwarcz.samuel.trempeasy.model.User;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;


public class TrempHelper {

    private static final String COLLECTION_NAME = "TrempList";
    private static int i =0;

    public static CollectionReference getTrempCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createTremp(final String uid,String mfrom, String mto, String mhour, String mplaces) {
       Tremp trempToCreate = new Tremp(uid, mfrom,mto ,mhour,mplaces);
        Date currentTime = Calendar.getInstance().getTime();
       String date= currentTime.toString();


        FirebaseDatabase.getInstance().getReference().child("ListTremp").child(uid).child(uid+date+(i++)).setValue(trempToCreate);

        return TrempHelper.getTrempCollection().document(uid+date).set(trempToCreate);
    }

    public static void joinTremp(final String useruid ,final String driverUID,final String trempUID,final int places) {

       // DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().getRef().child("Users").child(useruid);







        FirebaseDatabase.getInstance().getReference().child("ListTremp").child(driverUID).child(trempUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             Tremp st = dataSnapshot.getValue(Tremp.class);
             String rplaces = st.getRemainPlaces();
             int totplaces = Integer.parseInt(st.getPlaces());
             int remainPlaces = Integer.parseInt(rplaces);
            // remainPlaces = remainPlaces-places;
                String total="";
                for(int i= remainPlaces ; i>remainPlaces-places ; i--){
                    int u = totplaces-i+1;
                    String index = ""+u;
                    Log.d("test for",index);
                    FirebaseDatabase.getInstance().getReference().child("ListTremp").child(driverUID).child(trempUID).child("traveller").child(index).setValue(useruid);
                  total = ""+(i-1);
                  Log.d("test total",total);
                }


                FirebaseDatabase.getInstance().getReference().child("ListTremp").child(driverUID).child(trempUID).child("remainPlaces").setValue(total);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
