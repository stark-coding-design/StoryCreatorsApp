package com.stark.satos.storycreatorversion3.ui.actionadventuresection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stark.satos.storycreatorversion3.MainActivity;
import com.stark.satos.storycreatorversion3.R;
import com.stark.satos.storycreatorversion3.firestorefunctionality.AddStoryToMainStoryActivity;
import com.stark.satos.storycreatorversion3.firestorefunctionality.NewContentAdded;
import java.util.ArrayList;
import java.util.Objects;
import static com.stark.satos.storycreatorversion3.MainActivity.setStoryContentToView;

public class ActionAdventureStoryContentForRecyclerItem extends AppCompatActivity {
    TextView content;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private Button addToStoryButton;
    private Button cancelButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference actionCollectionRef = db.collection("action");
    DocumentReference storyIdDocRef = actionCollectionRef.document(setStoryContentToView.getStoryId());
    CollectionReference restOfContent = storyIdDocRef.collection("restOfContent");
    private final String whitespace = "     ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_adventure_story_content_for_recycler_item);

        content = findViewById(R.id.action_content_text_view);
        content.setMovementMethod(new ScrollingMovementMethod());
        setContent();
        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#006600"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        addToStoryButton = (Button) findViewById(R.id.content_submit);
        addToStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                Intent intent = new Intent(ActionAdventureStoryContentForRecyclerItem.this, AddStoryToMainStoryActivity.class);
                startActivity(intent);
            }
        });

        cancelButton = (Button) findViewById(R.id.cancel_button_action);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapActivity();
            }
        });
    }

    private void getContentFromDb() {
        String content = null;
        int j = 0;

        Task<QuerySnapshot> query = restOfContent.orderBy("timeStamp", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> contentForStory = new ArrayList<>();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                NewContentAdded newContentAdded = document.toObject(NewContentAdded.class);
                                String content = newContentAdded.getContent();
                                String authUserNAme = newContentAdded.getAuthUserName();
                                contentForStory.add(content + " (" + authUserNAme + ")");
                            }
                            addFinalContentToTextView(contentForStory);
                        }
                        else {
                            Exception e = task.getException();
                            System.out.println(e);
                        }
                    }
                });
    }

    private void addFinalContentToTextView(ArrayList<String> contentForStory) {

        //This will retrieve line separator dependent on OS.
        String newLine = System.getProperty("line.separator");
        String contentOriginalPost = setStoryContentToView.getStoryContent();
        String ogAuthor = setStoryContentToView.getOgAuthUsername();
        StringBuilder builder = new StringBuilder();

        for(String value : contentForStory){
            value.trim();
            builder.append(whitespace + value + newLine);
        }
        String text = builder.toString();
        contentOriginalPost.trim();
        content.setText(whitespace + contentOriginalPost + " (" +ogAuthor + ")" + newLine + text);
        setStoryContentToView.setStoryContent(whitespace + contentOriginalPost + " (" +ogAuthor + ")" + newLine + text);
    }

    public void swapActivity(){
        Intent intent = new Intent(ActionAdventureStoryContentForRecyclerItem.this, MainActivity.class);
        startActivity(intent);
    }

    private void setContent() {
        getContentFromDb();
    }
}