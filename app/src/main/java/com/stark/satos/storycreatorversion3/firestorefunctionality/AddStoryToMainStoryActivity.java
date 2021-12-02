package com.stark.satos.storycreatorversion3.firestorefunctionality;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stark.satos.storycreatorversion3.MainActivity;
import com.stark.satos.storycreatorversion3.R;
import java.sql.Timestamp;
import static com.stark.satos.storycreatorversion3.MainActivity.globalUserVar;
import static com.stark.satos.storycreatorversion3.MainActivity.setStoryContentToView;

public class AddStoryToMainStoryActivity extends AppCompatActivity {

    TextView mainStoryContentSection;
    EditText newStoryContentSection;
    private TextView wordCountView;
    Button cancelButton;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_a_users_story_layout);
        newStoryContentSection = findViewById(R.id.edit_text_view_drafts);
        mainStoryContentSection = findViewById(R.id.draft_content_text_view);
        cancelButton = findViewById(R.id.cancel_button_add_to_users_story);
        submitButton = findViewById(R.id.add_content_submit);
        wordCountView = findViewById(R.id.word_counter_add_to_main_story);
        newStoryContentSection.setMovementMethod(new ScrollingMovementMethod());
        mainStoryContentSection.setMovementMethod(new ScrollingMovementMethod());

        newStoryContentSection.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int length = newStoryContentSection.length();
                String convert = String.valueOf(length);
                wordCountView.setText(convert);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        setContent();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (newStoryContentSection.getText().length() == 0){
                    Toast.makeText(AddStoryToMainStoryActivity.this, "YOU HAVEN'T CREATED THE NEXT PART OF THE STORY YET!!" ,Toast.LENGTH_LONG );
                }
                else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference collectionRef = db.collection(setStoryContentToView.getGenre());
                    DocumentReference storyIdDocRef = collectionRef.document(setStoryContentToView.getStoryId());
                    CollectionReference restOfContentCollectionRef = storyIdDocRef.collection("restOfContent");
                    createDocumentForNewContentAdded(restOfContentCollectionRef);
                    Toast.makeText(AddStoryToMainStoryActivity.this, "WOO HOO!!  You Added to Someone's Story!!", Toast.LENGTH_LONG);
                    swapActivity();
                }
            }
        });

    cancelButton = (Button) findViewById(R.id.cancel_button_add_to_users_story);
        cancelButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            swapActivity();
        }
    });
}


    /**
     * @param restOfContentCollectionRef document reference where the existing story content is saved
     *
     * This method creates a document that holds the new story content that is to be added to a story
     * chosen by the user.  The current user's info will be saved i.e. username, uid, etc.
     */
    private void createDocumentForNewContentAdded(CollectionReference restOfContentCollectionRef) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserUid = mAuth.getCurrentUser().getUid();
        String newContentToAdd;
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        String timeStampToString = timeStamp.toString();
        String authUserName = globalUserVar.getProfileUserName();
        String content = newStoryContentSection.getText().toString();
        NewContentAdded newContentAdded = new NewContentAdded(authUserName, content, timeStampToString, currentUserUid);
        restOfContentCollectionRef.add(newContentAdded);
    }

    public void swapActivity(){
        Intent intent = new Intent(AddStoryToMainStoryActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void setContent() {
        mainStoryContentSection.setText(setStoryContentToView.getStoryContent());
    }
}