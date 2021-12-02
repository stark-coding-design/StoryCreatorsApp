package com.stark.satos.storycreatorversion3.firestorefunctionality;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.stark.satos.storycreatorversion3.R;

import org.jetbrains.annotations.NotNull;

public class AdapterScifi extends FirestoreRecyclerAdapter<NewPostDocumentFields, AdapterScifi.ViewHolder>{

    private final OnNoteListener mOnNoteListener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterScifi(@NonNull @NotNull FirestoreRecyclerOptions<NewPostDocumentFields> options, OnNoteListener onNoteListener) {
        super(options);
        this.mOnNoteListener = onNoteListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull AdapterScifi.ViewHolder holder, int position, @NonNull @NotNull NewPostDocumentFields model) {
        holder.newPost.setText(model.getTitle());
    }

    @NonNull
    @NotNull
    @Override
    public AdapterScifi.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View viewForRecycler = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_scifi, parent, false);

        return new AdapterScifi.ViewHolder(viewForRecycler, mOnNoteListener);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView newPost;
        AdapterScifi.OnNoteListener onNoteListener;

        public ViewHolder(@NonNull @NotNull View itemView,AdapterScifi.OnNoteListener onNoteListener) {
            super(itemView);
            newPost = itemView.findViewById(R.id.scifi_user_post_recycler_view_item_textview);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAbsoluteAdapterPosition());
        }
    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}