package com.example.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<NoteInfo> mNote;

    public NoteRecyclerAdapter(Context context, List<NoteInfo> notes) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mNote = notes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemToView = mLayoutInflater.inflate(R.layout.item_note_list, parent, false);
        return new ViewHolder(itemToView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        NoteInfo notes = mNote.get(position);
        viewHolder.mTextCourse.setText(notes.getCourse().getTitle());
        viewHolder.mTextTitle.setText(notes.getTitle());
        viewHolder.mCurrentPosition = position;


    }

    @Override
    public int getItemCount() {
        return mNote.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextCourse;
        public final TextView mTextTitle;
        public int mCurrentPosition;

        public ViewHolder(View itemView) {

            super(itemView);

            mTextCourse = itemView.findViewById(R.id.text_course);
            mTextTitle = itemView.findViewById(R.id.text_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_POSITION, mCurrentPosition);
                    mContext.startActivity(intent);

                }
            });
        }

    }
}
