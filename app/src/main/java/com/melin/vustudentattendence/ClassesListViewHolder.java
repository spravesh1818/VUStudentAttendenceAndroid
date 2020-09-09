package com.melin.vustudentattendence;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClassesListViewHolder extends RecyclerView.ViewHolder {
    TextView title,subtitle;


    public ClassesListViewHolder(@NonNull View itemView) {
        super(itemView);
        title=(TextView)itemView.findViewById(R.id.classesListTitle_textView);
        subtitle=(TextView)itemView.findViewById(R.id.classesListSubtitle_textView);
    }
}
