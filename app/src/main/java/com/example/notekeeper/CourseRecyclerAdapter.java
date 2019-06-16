package com.example.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<CourseInfo> mCourses;

    public CourseRecyclerAdapter(Context context, List<CourseInfo> courses) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mCourses = courses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemToView = mLayoutInflater.inflate(R.layout.item_course_list, parent, false);
        return new ViewHolder(itemToView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        CourseInfo courseInfo = mCourses.get(position);
        viewHolder.mTextCourse.setText(courseInfo.getTitle());
        viewHolder.mCurrentPosition = position;


    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextCourse;
        public int mCurrentPosition;

        public ViewHolder(View itemView) {

            super(itemView);

            mTextCourse = itemView.findViewById(R.id.text_course);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, mCourses.get(mCurrentPosition).getTitle(), Snackbar.LENGTH_LONG).show();
                }
            });
        }

    }
}
