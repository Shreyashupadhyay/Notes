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
import com.example.notes.listeners.Noteslistener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Noteslistener {
    public static final int Request_Code_Add_Note = 1;  // used for adding a notes
    public static final int Request_Code_Update_Note = 2;//used for updating notes

    private static final int Request_Code_show_Notes = 3;

    private RecyclerView notesrecyclerView;
    private List<Notes> notesList;
    private NotesAdapter notesAdapter;
    private int noteClickedPosition = -1;



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
        notesAdapter = new NotesAdapter(notesList, this);
        notesrecyclerView.setAdapter(notesAdapter);

        getNotes(Request_Code_show_Notes);


    }

    @Override
    public void onNoteClick(Notes notes, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), Notesedit.class);
        intent.putExtra("isViewOrUpdate" , true);
        intent.putExtra("note" , notes);
        startActivityForResult(intent, Request_Code_Update_Note); // this is been called means tha app is just started and we have show each every notes from the database that and request show update is usde to show database data of the notes

    }

    public void getNotes(final int requestCode){
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
                if(requestCode == Request_Code_show_Notes){
                    notesList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                } else if (requestCode == Request_Code_Add_Note) {
                    notesList.add(0,notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                    notesrecyclerView.smoothScrollToPosition(0);
                }else if(requestCode == Request_Code_Update_Note){
                    notesList.remove(noteClickedPosition);
                    notesList.add(noteClickedPosition, notes.get(noteClickedPosition));
                    notesAdapter.notifyItemChanged(noteClickedPosition);
                }
            }
        }
        new GetNotesTask().execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Request_Code_Add_Note && resultCode == RESULT_OK){
            getNotes(Request_Code_Add_Note);
        }else if(requestCode == Request_Code_Update_Note && resultCode == RESULT_OK){
            if(data != null){
                getNotes(Request_Code_Update_Note);
            }
        }
    }
}