package com.melin.vustudentattendence;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StudentListViewHolder extends RecyclerView.ViewHolder {
    TextView title,subtitle;

    public StudentListViewHolder(@NonNull View itemView) {
        super(itemView);
        title=(TextView)itemView.findViewById(R.id.studentListTitle_textView);
        subtitle=(TextView)itemView.findViewById(R.id.studentListSubtitle_textView);
    }
}
