package com.example.notes.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Index;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.database.NotesDatabase;
import com.example.notes.entities.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notesedit extends AppCompatActivity {

    private EditText inputNoteTitle , inputNotesSubtitle, inputNoteText;
    private TextView textDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notesedit);
        ImageView imageback = findViewById(R.id.imageBack);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNotesSubtitle = findViewById(R.id.inputNoteSubtitle);
        inputNoteText = findViewById(R.id.inputNote);
        textDateTime = findViewById(R.id.textdateTime);

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date())
        );

        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }

    private void saveNote(){
        if(inputNoteText.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Note Title can't be Empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputNotesSubtitle.getText().toString().trim().isEmpty()&& inputNoteText.getText().toString().trim()
                .isEmpty()) {
            Toast.makeText(this, "Notes Can't be Empty", Toast.LENGTH_SHORT).show();
            
            
        }

        final Notes notes = new Notes();
        notes.setTitle(inputNoteTitle.getText().toString());
        notes.setSubtitle(inputNotesSubtitle.getText().toString());
        notes.setNoteText(inputNoteText.getText().toString());
        notes.setDatetime(textDateTime.getText().toString());

        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void ,Void , Void>{
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getDatabase(getApplicationContext()).noteDao().insertNote(notes);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Intent intent = new Intent();
                setResult(RESULT_OK , intent);
                finish();
            }
        }

        new SaveNoteTask().execute();
    }
}