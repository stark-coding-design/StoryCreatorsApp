package com.stark.satos.storycreatorversion3.ui.create;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stark.satos.storycreatorversion3.R;
import com.stark.satos.storycreatorversion3.firestorefunctionality.CheckForUniqueStoryId;
import com.stark.satos.storycreatorversion3.ui.home.HomeFragment;
import com.stark.satos.storycreatorversion3.users.RandomNumber;
import static com.stark.satos.storycreatorversion3.MainActivity.globalUserVar;
import java.sql.Timestamp;
import java.util.UUID;

public class CreateStoryFragment extends Fragment {

    private CreateStoryViewModel createStoryViewModel;
    private Button saveToDraftsButton;
    private Button submitButton;
    private EditText createStoryBox;
    private EditText titleBox;
    private TextView wordCountView;
    private String title;
    private String newPost;
    private FirebaseFirestore db;
    private FirebaseDatabase dbForStories = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private String currentUserID;
    private String storyId;
    private String genre;
    private String submittedStoryUid = String.valueOf(UUID.randomUUID());
    private RandomNumber randomNumber = new RandomNumber();
    private final int numberOfChars = 20;
    private  Boolean storyIdNotUnique = true;
    private CheckForUniqueStoryId checkForUniqueStoryId = new CheckForUniqueStoryId();


    /**
     * submit a story into a specified genre OR save the story into the saved stories section (drafts)
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return return view
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        createStoryViewModel = new ViewModelProvider(this).get(CreateStoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_create_new_story, container, false);
        db = FirebaseFirestore.getInstance();
        createStoryBox = (EditText) root.findViewById(R.id.new_story_creation_box);
        titleBox = (EditText)root.findViewById(R.id.title_of_story);
        wordCountView = root.findViewById(R.id.word_counter);
        currentUserID = currentUser.getUid();
        String userNameTest = globalUserVar.getProfileUserName();

        addPopupList();

        createStoryBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int length = createStoryBox.length();
                String convert = String.valueOf(length);
                wordCountView.setText(convert);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        saveToDraftsButton = (Button) root.findViewById(R.id.save_to_drafts_button);
        saveToDraftsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (genre == null) {
                    Toast.makeText(getContext(), "PICK A GENRE!!", Toast.LENGTH_LONG).show();
                    addPopupList();
                } else if (checkForNull() == false) {
                    Toast.makeText(getContext(), "STORY MUST HAVE A TITLE AND CONTENT TO SUBMIT!!", Toast.LENGTH_LONG).show();
                }else{
                    Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
                    String timeStampToString = timeStamp.toString();
                    newPost = createStoryBox.getText().toString();
                    title = titleBox.getText().toString();
                    storyId = randomNumber.RandomNumber(numberOfChars);
                    checkForUniqueStoryId.checkForDraftsUniqueId(title, currentUserID, newPost, timeStampToString, genre, storyId);
                    swapFragment();
                    Toast.makeText(getContext(),"Story Saved Into Drafts!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        submitButton = (Button) root.findViewById(R.id.new_story_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (genre == null) {
                    Toast.makeText(getContext(), "PICK A GENRE!!", Toast.LENGTH_LONG).show();
                    addPopupList();
                } else if (checkForNull() == false) {
                    Toast.makeText(getContext(), "STORY MUST HAVE A TITLE AND CONTENT TO SUBMIT!!", Toast.LENGTH_LONG).show();
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference usersCollectionRef = db.collection("users");
                    Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
                    String timeStampToString = timeStamp.toString();
                    newPost = createStoryBox.getText().toString();
                    title = titleBox.getText().toString();
                    storyId = randomNumber.RandomNumber(numberOfChars);
                    checkForUniqueStoryId.checkForUniqueId(title, currentUserID, newPost, timeStampToString, genre, storyId, globalUserVar.getProfileUserName());
                    swapFragment();
                    Toast.makeText(getContext(), "WOOHOO!! YOUR STORY WAS SUBMITTED!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        return root;
    }

    /**
     * Check that there is content written in the title view and the main story content view
     * @return true if content != null; false if either content view == null.
     */
    private boolean checkForNull() {

        if(createStoryBox.getText().toString().trim().length() < 1){
            return false;
        }
        else if(titleBox.getText().toString().trim().length() < 1){
            return false;
        }
        else{
            return true;
        }
    }

    public void swapFragment(){

        HomeFragment newFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * add the pop up list so user can select genre for story
     */
    private void addPopupList(){

        String[] genres = {"Action/Adventure", "Sci-Fi", "Horror", "Fiction"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
}