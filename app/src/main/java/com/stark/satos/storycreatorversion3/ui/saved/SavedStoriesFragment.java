package com.stark.satos.storycreatorversion3.ui.saved;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.stark.satos.storycreatorversion3.R;
import com.stark.satos.storycreatorversion3.firestorefunctionality.DraftAttributes;
import com.stark.satos.storycreatorversion3.firestorefunctionality.DraftsListAdapter;

import static com.stark.satos.storycreatorversion3.MainActivity.setStoryContentToView;

public class SavedStoriesFragment extends Fragment implements DraftsListAdapter.OnNoteListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference newPostReference = db.collection("drafts");
    private final DocumentReference userStoryDocument = newPostReference.document(currentUser.getUid());
    private final CollectionReference userDraftsCollection = userStoryDocument.collection("userDrafts");
    private DraftsListAdapter adapter;
    private RecyclerView recyclerView;
    FirestoreRecyclerOptions<DraftAttributes> options;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_saved_creations, container, false);
        setUpRecyclerView(view);

        return view;
    }


    private void setUpRecyclerView(View view) {
        Query query = userDraftsCollection.orderBy("timeStamp", Query.Direction.DESCENDING);
        options = new FirestoreRecyclerOptions.Builder<DraftAttributes>()
                .setQuery(query, DraftAttributes.class).build();

        adapter = new DraftsListAdapter(options, this);
        recyclerView = view.findViewById(R.id.recycler_view_drafts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onNoteClick(int position) {

        ObservableSnapshotArray<DraftAttributes> docSnapshot = options.getSnapshots();
        String newPost =  docSnapshot.get(position).getNewPost();
        String newTitle = docSnapshot.get(position).getTitle();
        String storyId = docSnapshot.get(position).getStoryId();
        Intent intent = new Intent(getActivity(), DraftStoryContentForRecyclerItem.class);
        intent.putExtra("Note", newPost);
        setStoryContentToView.setStoryContent(newPost);
        setStoryContentToView.setTitle(newTitle);
        setStoryContentToView.setStoryId(storyId);
        startActivity(intent);
    }
}