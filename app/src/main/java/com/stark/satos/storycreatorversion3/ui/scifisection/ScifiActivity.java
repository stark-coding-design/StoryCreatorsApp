package com.stark.satos.storycreatorversion3.ui.scifisection;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.stark.satos.storycreatorversion3.R;
import com.stark.satos.storycreatorversion3.firestorefunctionality.AdapterScifi;
import com.stark.satos.storycreatorversion3.firestorefunctionality.NewPostDocumentFields;
import static com.stark.satos.storycreatorversion3.MainActivity.setStoryContentToView;

public class ScifiActivity extends AppCompatActivity implements AdapterScifi.OnNoteListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference newPostReference = db.collection("scifi");
    private AdapterScifi adapter;
    private RecyclerView recyclerView;

    FirestoreRecyclerOptions<NewPostDocumentFields> options;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scifi);
        setUpRecyclerView();

        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#6600cc"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

    }

    private void setUpRecyclerView() {
        Query query = newPostReference.orderBy("timeStamp", Query.Direction.DESCENDING);

        options = new FirestoreRecyclerOptions.Builder<NewPostDocumentFields>()
                .setQuery(query, NewPostDocumentFields.class).build();

        adapter = new AdapterScifi(options, this);
        recyclerView = findViewById(R.id.recycler_view_scifi);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onNoteClick(int position) {

        ObservableSnapshotArray<NewPostDocumentFields> docSnapshot = options.getSnapshots();
        String newPost =  docSnapshot.get(position).getNewPost();
        Intent intent = new Intent(this, ScifiStoryContentForRecyclerItem.class);
        intent.putExtra("Note", newPost);
        setStoryContentToView.setStoryContent(newPost);
        setStoryContentToView.setStoryId(docSnapshot.get(position).getStoryId());
        setStoryContentToView.setTitle(docSnapshot.get(position).getTitle());
        setStoryContentToView.setGenre("scifi");
        setStoryContentToView.setOgAuthUsername(docSnapshot.get(position).getUserName());
        startActivity(intent);
    }
}