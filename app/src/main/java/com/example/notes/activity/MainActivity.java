package com.example.notes.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.notes.R;
import com.example.notes.adapters.NotesAdapter;
import com.example.notes.database.NotesDatabase;
import com.example.notes.entities.Notes;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int Request_Code_Add_Note = 1;

    private RecyclerView notesrecyclerView;
    private List<Notes> notesList;
    private NotesAdapter notesAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);
        imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext() , Notesedit.class),Request_Code_Add_Note);

            }
        });

        notesrecyclerView= findViewById(R.id.NotesRecyclerview);
        notesrecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        );

        notesList = new ArrayList<>();
        notesAdapter = new NotesAdapter(notesList);
        notesrecyclerView.setAdapter(notesAdapter);

        getNotes();
    }
    public void getNotes(){
        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void, Void, List<Notes>>{
            @Override
            protected List<Notes> doInBackground(Void... voids) {
                return NotesDatabase
                        .getDatabase(getApplicationContext())
                        .noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Notes> notes) {
                super.onPostExecute(notes);
                if (notesList.size() == 0){
                    notesList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                }else{
                    notesList.add(0,notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                }
                notesrecyclerView.smoothScrollToPosition(0);
            }
        }
        new GetNotesTask().execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Request_Code_Add_Note && resultCode == RESULT_OK){
            getNotes();
        }
    }
}