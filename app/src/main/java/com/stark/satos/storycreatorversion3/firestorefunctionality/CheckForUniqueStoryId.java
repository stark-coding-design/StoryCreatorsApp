package com.stark.satos.storycreatorversion3.firestorefunctionality;


import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.stark.satos.storycreatorversion3.users.RandomNumber;


public class CheckForUniqueStoryId extends Fragment {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference storyIdCollectionRef;
    private DocumentReference storyIdDocumentReference;
    private final RandomNumber randomNumber = new RandomNumber();
    private final int numberOfChars = 20;
    private String newRandomNumber;


    /**
     * @param title title of story
     * @param currentUserID current user id
     * @param newPost content to be added for this FIRST post of the story
     * @param timeStampToString time post was submitted
     * @param genre genre of story
     * @param uniqueStoryId story id number
     * @param userName current user's username
     *
     * Method ensures that a new post does not have the same id number as a prior story post
     * -- creates new id number if one already exists
     *
     * */
    public void checkForUniqueId(String title, String currentUserID, String newPost, String timeStampToString, String genre, String uniqueStoryId, String userName){

        storyIdCollectionRef = db.collection("userStoryIDs");
        storyIdDocumentReference = storyIdCollectionRef.document("listOfStoryIDs");

        Query query = storyIdCollectionRef.whereArrayContains("storyIdList", uniqueStoryId);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size() == 0){
                    AddOriginalPostAndTitle createdNewStory = new AddOriginalPostAndTitle(title, currentUserID, newPost, timeStampToString, genre, uniqueStoryId, userName);
                }
                else {
                    newRandomNumber = randomNumber.RandomNumber(numberOfChars);
                    checkForUniqueId(title, currentUserID, newPost, timeStampToString, genre, newRandomNumber, userName);
                }
            }
        });
    }

    /**
     * @param title title of story
     * @param currentUserID current user id
     * @param newPost content to be added for this FIRST post of the story
     * @param timeStampToString time post was submitted
     * @param genre genre of story
     * @param uniqueStoryId story id number
     *
     * Method ensures that a new post does not have the same id number as a prior story post
     * -- creates new id number if one already exists
     *
     * */
    public void checkForDraftsUniqueId(String title, String currentUserID, String newPost, String timeStampToString, String genre, String uniqueStoryId){

        storyIdCollectionRef = db.collection("userStoryIDs");
        storyIdDocumentReference = storyIdCollectionRef.document("listOfStoryIDs");

        Query query = storyIdCollectionRef.whereArrayContains("storyIdList", uniqueStoryId);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size() == 0){
                    AddOriginalPostAndTitle createdNewStory = new AddOriginalPostAndTitle();
                    createdNewStory.AddUserNewPostsToDrafts(title, currentUserID, newPost, timeStampToString, genre, uniqueStoryId);
                }
                else {
                    newRandomNumber = randomNumber.RandomNumber(numberOfChars);
                    checkForDraftsUniqueId(title, currentUserID, newPost, timeStampToString, genre, newRandomNumber);
                }
            }
        });
    }
}