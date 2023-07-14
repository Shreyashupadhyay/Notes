package com.example.notes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.entities.Notes;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {


    private List<Notes> notes;

    public NotesAdapter(List<Notes> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_notes,parent,false)
        );
    }

    @Override
    public void onBindViewHolder( NoteViewHolder holder, int position) {
        holder.setNote(notes.get(position));

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

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTtile);
            textSubtitle =itemView.findViewById(R.id.textsubtitle);
            textDateTime = itemView.findViewById(R.id.textdateTime);
        }
        void setNote(Notes notes){
            textTitle.setText(notes.getTitle());
            if(notes.getSubtitle().trim().isEmpty()){
                textSubtitle.setVisibility(View.GONE);
            }else{
                textSubtitle.setText(notes.getSubtitle());
            }
//            textDateTime.setText(notes.getDatetime());

        }
    }
}
