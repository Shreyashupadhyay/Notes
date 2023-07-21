package com.example.notes.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Index;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.database.NotesDatabase;
import com.example.notes.entities.Notes;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notesedit extends AppCompatActivity {

    private EditText inputNoteTitle , inputNotesSubtitle, inputNoteText;
    private TextView textDateTime;
    private View viewsubtitleselector;

    private String selectedNoteColor;

    private String selectedImagePath;

    private ImageView imageNote;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;

    private static final int REQUEST_CODE_SELECT_iMAGE = 2;

    // private AlertDialog dialogAddURL;

    private Notes alreadyAvailiable;

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
        viewsubtitleselector = findViewById(R.id.viewSubtitleIndicator);
        imageNote = findViewById(R.id.imageNote);

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

        selectedNoteColor = "#333333";
        selectedImagePath = "";

        if(getIntent().getBooleanExtra("isViewOrUpdate", false)){
            alreadyAvailiable = (Notes) getIntent().getSerializableExtra("note");
            setViewOrUpdate();
        }

        initmiscellanous();
        setSubtitleIndiactoreColor();
    }

    private void setViewOrUpdate(){
        inputNoteTitle.setText(alreadyAvailiable.getTitle());
        inputNotesSubtitle.setText(alreadyAvailiable.getSubtitle());
        inputNoteText.setText(alreadyAvailiable.getNoteText());
        textDateTime.setText(alreadyAvailiable.getDatetime());
        if(alreadyAvailiable.getImagePath() != null && !alreadyAvailiable.getImagePath().trim().isEmpty()){
            imageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailiable.getImagePath()));
            imageNote.setVisibility(View.VISIBLE);
            selectedImagePath = alreadyAvailiable.getImagePath();
        }
//        if(alreadyAvailiable.getWebLink() != null && !alreadyAvailiable.getWebLink().trim().isEmpty()){
//
//        }

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
        notes.setColor(selectedNoteColor);
        notes.setImagePath(selectedImagePath);

        if(alreadyAvailiable != null){
            notes.setId(alreadyAvailiable.getId());
        }

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

    private void initmiscellanous(){
        final LinearLayout layoutMiscllanous = findViewById(R.id.layoutMiscellanous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscllanous);
        layoutMiscllanous.findViewById(R.id.textMis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        final ImageView imageColor1 = layoutMiscllanous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMiscllanous.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMiscllanous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMiscllanous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMiscllanous.findViewById(R.id.imageColor5);

        layoutMiscllanous.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#333333";
                imageColor1.setImageResource(R.drawable.round_done_24);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);

            }
        });
        layoutMiscllanous.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FDBE3B";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.round_done_24);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);

            }
        });
        layoutMiscllanous.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FF4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.round_done_24);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);

            }
        });
        layoutMiscllanous.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#3A52FC";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.round_done_24);
                imageColor5.setImageResource(0);

            }
        });
        layoutMiscllanous.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#000000";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.round_done_24);

            }
        });
         if(alreadyAvailiable != null && alreadyAvailiable.getColor() != null && !alreadyAvailiable.getColor().trim().isEmpty()){
             switch ((alreadyAvailiable.getColor())){
                 case "#FDBE3B":
                     layoutMiscllanous.findViewById(R.id.viewColor2).performClick();
                     break;
                 case "#FF4842":
                     layoutMiscllanous.findViewById(R.id.viewColor3).performClick();
                     break;
                 case "#3A52FC":
                     layoutMiscllanous.findViewById(R.id.viewColor4).performClick();
                     break;
                 case "#000000":
                     layoutMiscllanous.findViewById(R.id.viewColor5).performClick();
                     break;
             }
         }
         layoutMiscllanous.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                 if(ContextCompat.checkSelfPermission(
                         getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                         )!= PackageManager.PERMISSION_GRANTED){
                     ActivityCompat.requestPermissions(
                             Notesedit.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_STORAGE_PERMISSION
                     );
                 }else {
                     selectImage();
                 }

             }
         });



    }
    private void setSubtitleIndiactoreColor(){
        GradientDrawable gradientDrawable = (GradientDrawable) viewsubtitleselector.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }

   
    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_CODE_SELECT_iMAGE);
        }
//        try {
//            startActivityForResult(intent,REQUEST_CODE_SELECT_iMAGE);
//        }catch (ActivityNotFoundException e){
//            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length>0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_SELECT_iMAGE && resultCode == RESULT_OK){
            if(data != null){
                Uri selectImageUri =data.getData();
                if(selectImageUri != null){
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageNote.setImageBitmap(bitmap);
                        imageNote.setVisibility(View.VISIBLE);
                        selectedImagePath = getPathFromUri(selectImageUri);

                    }catch (Exception exception){
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    private String getPathFromUri(Uri contentUri){
        String filePath;
        Cursor cursor = getContentResolver()
                .query(contentUri , null , null, null, null);
        if(cursor == null){
            filePath = contentUri.getPath();
        }else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }
}