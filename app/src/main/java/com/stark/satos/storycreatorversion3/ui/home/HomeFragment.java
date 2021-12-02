package com.stark.satos.storycreatorversion3.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.stark.satos.storycreatorversion3.R;
import com.stark.satos.storycreatorversion3.ui.actionadventuresection.ActionAdventureActivity;
import com.stark.satos.storycreatorversion3.ui.fictionsection.FictionSectionActivity;
import com.stark.satos.storycreatorversion3.ui.horrorsection.HorrorSectionActivity;
import com.stark.satos.storycreatorversion3.ui.scifisection.ScifiActivity;
import com.stark.satos.storycreatorversion3.ui.selectgalleryimage.UserImageSelect;
import com.stark.satos.storycreatorversion3.users.User;
import static com.stark.satos.storycreatorversion3.MainActivity.globalUserVar;


public class HomeFragment extends Fragment {

    private Button horrorSectionButton;
    private Button fictionSectionButton;
    private Button actionAdventureButton;
    private Button scifiButton;
    private ImageButton profilePicButton;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private TextView userNameOnHomeFragment;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_home, container, false);
        userNameOnHomeFragment = (TextView) root.findViewById(R.id.userNameOnProfilePage);
        profilePicButton = root.findViewById(R.id.profile_image);

        readUserNameData(new FirestoreCallbackGetUserName() {
            @Override
            public void onCallback(String userName) {
                userNameOnHomeFragment.setText(userName);
            }
        });


        readImageData(new FirestoreCallbackGetImage() {
            @Override
            public void onCallbackProfileImage(Uri uri) {
                Picasso.get().load(uri.toString()).fit().centerCrop().into(profilePicButton);
            }
        });

        horrorSectionButton = (Button) root.findViewById(R.id.genreTabHorror);
        horrorSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                swapActivity(HorrorSectionActivity.class);
            }
        });

        fictionSectionButton = (Button) root.findViewById(R.id.genreTabFiction);
        fictionSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                swapActivity(FictionSectionActivity.class);
            }
        });

        actionAdventureButton = (Button) root.findViewById(R.id.genreTabActionAdventure);
        actionAdventureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                swapActivity(ActionAdventureActivity.class);
            }
        });

        scifiButton = (Button) root.findViewById(R.id.genreTabSciFi);
        scifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                swapActivity(ScifiActivity.class);
            }
        });

        profilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                swapActivity(UserImageSelect.class);
            }
        });

        return root;
    }


    private interface FirestoreCallbackGetUserName {
        void onCallback(String userName);
    }

    private interface FirestoreCallbackGetImage{
        void onCallbackProfileImage(Uri uri);
    }

    private void readImageData(FirestoreCallbackGetImage firestoreCallbackGetImage){
        String uid = currentUser.getUid();
        StorageReference ref = storageReference.child("uids/").child(uid + "/").child("image");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                firestoreCallbackGetImage.onCallbackProfileImage(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println(exception);
            }
        });
    }

    private void readUserNameData(FirestoreCallbackGetUserName firestoreCallbackGetUserName){
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                firestoreCallbackGetUserName.onCallback(user.getProfileUserName());
                globalUserVar.setProfileUserName(user.getProfileUserName());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
               System.out.println(exception);
            }
        });
    }

    private void swapActivity(Class activity) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
    }
}