package com.stark.satos.storycreatorversion3.firestorefunctionality;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.stark.satos.storycreatorversion3.ui.create.CreateStoryFragment;
import com.stark.satos.storycreatorversion3.users.User;

public class AddDataToFirestore {

    CreateStoryFragment forToastMessage = new CreateStoryFragment();
    public AddDataToFirestore() {
    }

    /**
     * @param profileUserName user name chosen by user
     *
     *  Adds user info (user name, email, and password) into the FireStore Database
     */
    public AddDataToFirestore(String profileUserName, String userEmail, String userPassword, String profilePicUrl) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        CollectionReference dbUserInfo = db.collection("users");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userUID = currentUser.getUid();

        User userToFirestore = new User(profileUserName, userEmail, userPassword, profilePicUrl);
        dbUserInfo.document(userUID).set(userToFirestore, SetOptions.merge());
    }
}