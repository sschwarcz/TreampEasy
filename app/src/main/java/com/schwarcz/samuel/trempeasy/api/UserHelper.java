package com.schwarcz.samuel.trempeasy.api;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.schwarcz.samuel.trempeasy.model.User;

public class UserHelper {

    private static final String COLLECTION_NAME = "Users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username,String fullName ,String Email , String telephone) {
        User userToCreate = new User(uid, username,fullName ,Email,telephone);

        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(userToCreate);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }


  

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

}
