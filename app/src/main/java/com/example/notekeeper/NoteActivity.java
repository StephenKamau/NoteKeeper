package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    public static final String NOTE_POSITION = "com.example.notekeeper.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.example.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.example.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.example.notekeeper.ORIGINAL_NOTE_TEXT";

    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner mSpinnerCourses;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private int mNotePosition;
    private boolean mIsCancelling;
    private String mOriginalNoteCourseId;
    private String mOriginalNoteTitle;
    private String mOriginalNoteText;

    @Override
    protected void onPause() {
        super.onPause();
        if (mIsCancelling) {
            if (mIsNewNote) {
                DataManager.getInstance().removeNote(mNotePosition);
            } else {
                storePreviousNoteValues();
            }
        } else {
            saveNote();
        }
    }

    private void storePreviousNoteValues() {
        CourseInfo courseInfo = DataManager.getInstance().getCourse(mOriginalNoteCourseId);
        mNote.setTitle(mOriginalNoteTitle);
        mNote.setText(mOriginalNoteText);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) mSpinnerCourses.getSelectedItem());
        mNote.setTitle(mTextNoteTitle.getText().toString());
        mNote.setText(mTextNoteText.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSpinnerCourses = findViewById(R.id.spinner_courses);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> courseInfoArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        courseInfoArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(courseInfoArrayAdapter);
        readDisplayStateValues();

        if (savedInstanceState == null) {
            saveOriginalNoteValues();
        } else {
            restoreOriginalNoteValues(savedInstanceState);
        }

        mTextNoteTitle = findViewById(R.id.text_note_title);
        mTextNoteText = findViewById(R.id.text_note_text);

        if (!mIsNewNote)
            displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);
    }

    private void restoreOriginalNoteValues(Bundle savedInstanceState) {
        mOriginalNoteCourseId = savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
        mOriginalNoteTitle = savedInstanceState.getString(ORIGINAL_NOTE_TITLE);
        mOriginalNoteText = savedInstanceState.getString(ORIGINAL_NOTE_TEXT);

    }

    private void saveOriginalNoteValues() {
        if (mIsNewNote)
            return;
        mOriginalNoteCourseId = mNote.getCourse().getCourseId();
        mOriginalNoteTitle = mNote.getTitle();
        mOriginalNoteText = mNote.getText();
    }

    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int index = courses.indexOf(mNote.getCourse());
        spinnerCourses.setSelection(index);
        textNoteTitle.setText(mNote.getTitle());
        textNoteText.setText(mNote.getText());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ORIGINAL_NOTE_COURSE_ID, mOriginalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE, mOriginalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT, mOriginalNoteText);
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mIsNewNote = position == POSITION_NOT_SET;

        if (mIsNewNote) {
            createNewNote();
        } else {
            mNote = DataManager.getInstance().getNotes().get(position);
        }
    }

    private void createNewNote() {
        DataManager dataManager = DataManager.getInstance();
        mNotePosition = dataManager.createNewNote();
        mNote = dataManager.getNotes().get(mNotePosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_email) {
            sendEmail();
            return true;
        } else if (id == R.id.action_cancel) {
            mIsCancelling = true;
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendEmail() {
        CourseInfo course = (CourseInfo) mSpinnerCourses.getSelectedItem();
        String subject = mTextNoteTitle.getText().toString();
        String text = "Checkout what I learned in the PluralSight course \"" + course.getTitle() + "\"\n" + mTextNoteText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rcf2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }
}
