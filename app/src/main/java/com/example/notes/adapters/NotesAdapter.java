package com.example.notes.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.entities.Notes;
import com.example.notes.listeners.Noteslistener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {


    private final List<Notes> notes;
    private Noteslistener noteslistener;


    public NotesAdapter(List<Notes> notes , Noteslistener noteslistener) {
        this.notes = notes;
        this.noteslistener = noteslistener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_notes,
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder( NoteViewHolder holder, int position) {
        holder.setNote(notes.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteslistener.onNoteClick(notes.get(position) , position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle , textSubtitle , textDateTime;
        LinearLayout layoutNote;

        RoundedImageView imageNote;


        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTtile);
            textSubtitle =itemView.findViewById(R.id.textsubtitle);
            textDateTime = itemView.findViewById(R.id.textdateTime);
            layoutNote = itemView.findViewById(R.id.layoutnote);
            imageNote = itemView.findViewById(R.id.imageNote);
        }
        void setNote(Notes notes){
            textTitle.setText(notes.getTitle());
            if(notes.getSubtitle().trim().isEmpty()){
                textSubtitle.setVisibility(View.GONE);
            }else{
                textSubtitle.setText(notes.getSubtitle());
            }
//            textDateTime.setText(notes.getDatetime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if(notes.getColor() != null){
                gradientDrawable.setColor(Color.parseColor(notes.getColor()));
            }else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }
            if(notes.getImagePath() != null){
                imageNote.setImageBitmap(BitmapFactory.decodeFile(notes.getImagePath()));
                imageNote.setVisibility(View.VISIBLE);

            }else {
                imageNote.setVisibility(View.GONE);
            }
        }
    }
}
