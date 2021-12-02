package com.stark.satos.storycreatorversion3.ui.fictionsection;

import android.content.Intent;
import android.os.Bundle;
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
import com.stark.satos.storycreatorversion3.firestorefunctionality.AdapterFiction;
import com.stark.satos.storycreatorversion3.firestorefunctionality.NewPostDocumentFields;
import static com.stark.satos.storycreatorversion3.MainActivity.setStoryContentToView;

public class FictionSectionActivity extends AppCompatActivity implements AdapterFiction.OnNoteListener {

    private FirebaseAuth  mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference newPostReference = db.collection("fiction");
    private AdapterFiction adapter;
    private RecyclerView recyclerView;

    FirestoreRecyclerOptions<NewPostDocumentFields> options;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiction);
        setUpRecyclerView();

    }

    private void setUpRecyclerView() {

        Query query = newPostReference.orderBy("timeStamp", Query.Direction.DESCENDING);
        options  = new FirestoreRecyclerOptions.Builder<NewPostDocumentFields>()
                .setQuery(query, NewPostDocumentFields.class).build();
        adapter = new AdapterFiction(options, this);
        recyclerView = findViewById(R.id.recycler_view_fiction);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        ObservableSnapshotArray<NewPostDocumentFields> docSnapshot = options.getSnapshots();
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
          Intent intent = new Intent(this, FictionStoryContentForRecyclerItem.class);
          intent.putExtra("Note", newPost);
          setStoryContentToView.setStoryContent(newPost);
          setStoryContentToView.setStoryId(docSnapshot.get(position).getStoryId());
          setStoryContentToView.setTitle(docSnapshot.get(position).getTitle());
          setStoryContentToView.setOgAuthUsername(docSnapshot.get(position).getUserName());
          setStoryContentToView.setGenre("fiction");
          startActivity(intent);
    }
}