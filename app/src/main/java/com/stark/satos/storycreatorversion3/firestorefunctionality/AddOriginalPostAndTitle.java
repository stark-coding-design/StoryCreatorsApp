package com.stark.satos.storycreatorversion3.firestorefunctionality;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class AddOriginalPostAndTitle {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userName;

    public AddOriginalPostAndTitle() {
    }


    /**
     * @param title title of story
     * @param userId current user id
     * @param newPost content to be added for this FIRST post of the story
     * @param timeStamp time post was submitted
     * @param genre genre of story
     * @param storyId story id number
     * @param userName current user's username
     *
     *  This method adds the first post and author's information into the Firestore DB
     */
    public AddOriginalPostAndTitle(String title, String userId, String newPost, String timeStamp, String genre, String storyId, String userName) {
        CollectionReference genreCollection;
        DocumentReference originalPostDocument;
        CollectionReference wholeStoryContentCollection;
        DocumentReference wholeStoryContentDocument;
        CollectionReference newPostsForStory;
        String collectionPath;

        CollectionReference storyIdCollection;
        DocumentReference storyIdList;
        storyIdCollection = db.collection("userStoryIDs");
        storyIdList = storyIdCollection.document("listOfStoryIDs");

        genreCollection = db.collection(genre);
        originalPostDocument = genreCollection.document(storyId);

        switch(genre) {
            case "action":
                genre = "Action";
                break;
            case "scifi":
                genre = "Scifi";
                break;
            case "horror":
                genre = "Horror";
                break;
            case "fiction":
                genre = "Fiction";
                break;
        }

        wholeStoryContentCollection =originalPostDocument.collection(genre);
        wholeStoryContentDocument = wholeStoryContentCollection.document(storyId);
        NewPostDocumentFields newPostDocumentFields = new NewPostDocumentFields(newPost, timeStamp, title, storyId, userId, userName);
        originalPostDocument.set(newPostDocumentFields, SetOptions.merge());

        //Automatically add a new storyId to the "storyIdList" array field.
        storyIdList.update("storyIdList", FieldValue.arrayUnion(storyId));
    }


    /**
     * @param title title of story
     * @param userId current user id
     * @param newPost content to be added for this FIRST post of the story
     * @param timeStamp time post was submitted
     * @param genre genre of story
     * @param storyId story id number
     *
     * This method saves a user's draft into th DB
     */
    public void AddUserNewPostsToDrafts(String title, String userId, String newPost, String timeStamp, String genre, String storyId) {
        CollectionReference draftsCollectionRef;
        DocumentReference uidDocRef;
        CollectionReference userDraftsCollectionRef;
        DocumentReference storyIdDocRef;
        CollectionReference storyIdCollectionRef;
        DocumentReference storyIdDocumentReference2;

        draftsCollectionRef = db.collection("drafts");
        uidDocRef = draftsCollectionRef.document(userId);
        userDraftsCollectionRef = uidDocRef.collection("userDrafts");
        storyIdDocRef = userDraftsCollectionRef.document(storyId);
        storyIdCollectionRef = db.collection("userStoryIDs");
        storyIdDocumentReference2 = storyIdCollectionRef.document("listOfStoryIDs");


        switch(genre) {
            case "action":
                genre = "Action";
                break;
            case "scifi":
                genre = "Scifi";
                break;
            case "horror":
                genre = "Horror";
                break;
            case "fiction":
                genre = "Fiction";
                break;
        }

        DraftAttributes newDraftAttributes = new DraftAttributes(title, newPost, storyId, timeStamp);
        storyIdDocRef.set(newDraftAttributes, SetOptions.merge());

        //Automatically add a new storyId to the "storyIdList" array field.
       storyIdDocumentReference2.update("storyIdList", FieldValue.arrayUnion(storyId));

    }
}
