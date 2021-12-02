package com.stark.satos.storycreatorversion3.ui.fictionsection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
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

public class FictionStoryContentForRecyclerItem extends AppCompatActivity {
    private TextView content;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private Button addToStoryButton;
    private Button cancelButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference fictionCollectionRef = db.collection("fiction");
    private DocumentReference storyIdDocRef = fictionCollectionRef.document(setStoryContentToView.getStoryId());
    private CollectionReference restOfContent = storyIdDocRef.collection("restOfContent");
    private final String whitespace = "     ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiction_story_content_for_recycler_item);
        content = findViewById(R.id.fiction_content_text_view);
        content.setMovementMethod(new ScrollingMovementMethod());
        setContent();

        addToStoryButton = (Button) findViewById(R.id.content_submit);
        addToStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                Intent intent = new Intent(FictionStoryContentForRecyclerItem.this, AddStoryToMainStoryActivity.class);
                startActivity(intent);
            }
        });

        cancelButton = (Button) findViewById(R.id.cancel_button_for_fiction);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapActivity();
            }
        });
    }

    private void setContent() {
        getContentFromDb();
     }


    private void getContentFromDb() {
        String content = null;

        Task<QuerySnapshot> query = restOfContent.orderBy("timeStamp", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> contentForStory = new ArrayList<>();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                NewContentAdded newContentAdded = document.toObject(NewContentAdded.class);
                                String content = newContentAdded.getContent();
                                String authUserName = newContentAdded.getAuthUserName();
                                contentForStory.add(content + " (" + authUserName + ")");
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
        content.setText(whitespace + contentOriginalPost + " (" + ogAuthor + ")" + newLine + text);
        setStoryContentToView.setStoryContent(whitespace + contentOriginalPost + " (" + ogAuthor + ")" + newLine + text);
    }


    public void swapActivity(){
        Intent intent = new Intent(FictionStoryContentForRecyclerItem.this, MainActivity.class);
        startActivity(intent);
    }
}