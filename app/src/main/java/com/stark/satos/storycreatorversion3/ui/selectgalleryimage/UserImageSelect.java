package com.stark.satos.storycreatorversion3.ui.selectgalleryimage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stark.satos.storycreatorversion3.MainActivity;
import com.stark.satos.storycreatorversion3.R;
import java.io.IOException;
import java.util.UUID;
import static com.stark.satos.storycreatorversion3.MainActivity.globalUserVar;

public class UserImageSelect extends AppCompatActivity {

    private Button selectImage;
    private ImageView imageToBeSelected;
    private Button saveImage;
    private final int SELECT_IMAGE = 200;
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = firebaseStorage.getReference();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private  Uri selectedImageUri;
    private final String randomUuidForProfilePic = UUID.randomUUID().toString();
    private String imageNameFromCloudDatabase;
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String uid = currentUser.getUid();
    StorageReference ref = storageReference.child("uids/").child(uid + "/").child("image");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_image_select);

        selectImage = findViewById(R.id.userImageSelectButton);
        imageToBeSelected = findViewById(R.id.userImageSelectViewBox);
        saveImage = findViewById(R.id.userImageSaveButton);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri != null) {
                    saveImage(new FirestoreCallbackSaveImage() {
                        @Override
                        public void onCallbackSaveImage(String imageUrl) {
                            globalUserVar.setProfilePicUrl(imageUrl);
                            swapActivity();
                        }
                    });
                }
            }
        });
    }

    public void swapActivity(){
        Intent intent = new Intent(UserImageSelect.this, MainActivity.class);
        startActivity(intent);
    }

    private interface FirestoreCallbackSaveImage{
        void onCallbackSaveImage(String imageUrl);
    }

    private void saveImage(FirestoreCallbackSaveImage firestoreCallbackSaveImage){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Image...");
        progressDialog.show();

        ref.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                String profilePicUrl = ref.toString();
                setUserProfilePicUrl(profilePicUrl);
                firestoreCallbackSaveImage.onCallbackSaveImage(profilePicUrl);
                String currentUserID = currentUser.getUid();

                // Image uploaded successfully
                // Dismiss dialog
                progressDialog.dismiss();

                CollectionReference dbUserCollection = db.collection("users");
                dbUserCollection.document(currentUserID).update("profilePicUrl", profilePicUrl);
                Toast.makeText(UserImageSelect.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast.makeText(UserImageSelect.this,"Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(
                        new OnProgressListener<UploadTask.TaskSnapshot>() {

                            // Progress Listener for loading
                            // percentage on the dialog box
                            @Override
                            public void onProgress(
                                    UploadTask.TaskSnapshot taskSnapshot)
                            {
                                double progress
                                        = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int)progress + "%");
                            }
                        });
    }

    private void setUserProfilePicUrl(String profilePicUrl) {
        globalUserVar.setProfilePicUrl(profilePicUrl);
    }

    private void chooseImage() {

        Intent imageIntent = new Intent();
        imageIntent.setType("image/*");
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(imageIntent, "Select Picture"), SELECT_IMAGE);

    }
    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_IMAGE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                try {
                    // Setting image on image view using Bitmap
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    imageToBeSelected.setImageBitmap(bitmap);
                }
                catch (IOException e) {
                    // Log the exception
                    e.printStackTrace();
                }
            }
        }
    }
}