package com.stark.satos.storycreatorversion3.ui.saved;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stark.satos.storycreatorversion3.MainActivity;
import com.stark.satos.storycreatorversion3.R;
import com.stark.satos.storycreatorversion3.firestorefunctionality.AddOriginalPostAndTitle;
import java.sql.Timestamp;
import java.util.Date;
import static com.stark.satos.storycreatorversion3.MainActivity.setStoryContentToView;
import static com.stark.satos.storycreatorversion3.MainActivity.globalUserVar;

public class DraftStoryContentForRecyclerItem extends AppCompatActivity {

    private EditText content;
    private EditText title;
    private String contentText;
    private String titleText;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = mAuth.getCurrentUser();
    private final String currentUserID = currentUser.getUid();
    private final String userName = globalUserVar.getProfileUserName();
    private String delete;
    private Button submitButton;
    private Button cancelButton;
    private Button deleteButton;
    private String genre;
    private String storyId;
    private TextView wordCountViewDrafts;
    private CoordinatorLayout layout;

    /**
     * Submit draft into a genre OR cancel draft and go back to home screen
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_drafts_content_for_recycler_item);
        content = findViewById(R.id.draft_content_text_view);
        wordCountViewDrafts = findViewById(R.id.word_counter_drafts);
        title = findViewById(R.id.titleTextBox);
        storyId = setStoryContentToView.getStoryId();
        submitButton = findViewById(R.id.draft_content_submit);
        cancelButton = findViewById(R.id.draft_cancel_button);
        deleteButton = findViewById(R.id.draft_delete_button);
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int length = content.length();
                String convert = String.valueOf(length);
                wordCountViewDrafts.setText(convert);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        setContent();
        addPopupList();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showCustomDialog();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (genre == null) {
                    Toast.makeText(DraftStoryContentForRecyclerItem.this, "PICK A GENRE!!", Toast.LENGTH_LONG).show();
                    addPopupList();
                } else if (checkForNull() == false) {
                    Toast.makeText(DraftStoryContentForRecyclerItem.this, "STORY MUST HAVE A TITLE AND CONTENT TO SUBMIT!!", Toast.LENGTH_LONG).show();
                } else {

                    //Get userName by querying "users" collection
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference usersCollectionRef = db.collection("users");
                    Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
                    String timeStampToString = timeStamp.toString();
                    contentText = content.getText().toString();
                    titleText = title.getText().toString();

                    //TODO Add a textview inside the recycler view item to accommodate the author's name and date of post
                    Date date = new Date(timeStamp.getTime());

                    AddOriginalPostAndTitle createdNewStory = new AddOriginalPostAndTitle(titleText, currentUserID, contentText, timeStampToString, genre, storyId, userName);
                    deleteDocumentFromDrafts();
                    swapActivity();
                    Toast.makeText(DraftStoryContentForRecyclerItem.this, "WOOHOO!! YOUR STORY WAS SUBMITTED!!", Toast.LENGTH_LONG).show();
                }
            }

        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapActivity();
            }
        });
    }

    private void showCustomDialog() {

        final Button yesButton;
        final Button noButton;
        final Dialog dialog = new Dialog(DraftStoryContentForRecyclerItem.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //user can cancel window by clicking outside of it
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_delete_draft);

        yesButton = dialog.findViewById(R.id.yes_delete_button);
        noButton = dialog.findViewById(R.id.do_not_delete_button);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteDocumentFromDrafts();
                dialog.dismiss();
            //    swapToSavedCreations();
                swapActivity();

            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void swapToSavedCreations() {
        /*
        Fragment n = new SavedStoriesFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.saved_creations, n);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
         */
    }


    public void swapActivity(){
        Intent intent = new Intent(DraftStoryContentForRecyclerItem.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * add the pop up list so user can select genre for story
     */
    private void addPopupList() {
        String[] genres = {"Action/Adventure", "Sci-Fi", "Horror", "Fiction"};

        AlertDialog.Builder builder = new AlertDialog.Builder(DraftStoryContentForRecyclerItem.this);
        builder.setTitle("Pick a Genre");
        builder.setItems(genres, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch(which) {
                    case 0:
                        genre = "action";
                        break;
                    case 1:
                        genre = "scifi";
                        break;
                    case 2:
                        genre = "horror";
                        break;
                    case 3:
                        genre = "fiction";
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * get the content of the recycler (was put to setters in another method) from
     * SetStoryContentToView() and set to the content and title views
     */
    private void setContent() {
        content.setText(setStoryContentToView.getStoryContent());
        title.setText(setStoryContentToView.getTitle());
    }

    /**
     * Check that there is content written in the title view and the main story content view
     * @return true if content != null; false if either content view == null.
     */
    private boolean checkForNull() {

        if(content.getText().toString().trim().length() < 1){
            return false;
        }
        else if(title.getText().toString().trim().length() < 1){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Deletes saved story drafts from the database "drafts" collection
     *
     */
    private void deleteDocumentFromDrafts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();
        CollectionReference draftsCollectionRef = db.collection("drafts");
        DocumentReference userDraftsDocument = draftsCollectionRef.document(currentUserId);
        CollectionReference userDraftsByUidCollectionRef = userDraftsDocument.collection("userDrafts");

        userDraftsByUidCollectionRef.document(storyId).delete();
    }
}