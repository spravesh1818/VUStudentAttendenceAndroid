package com.melin.vustudentattendence;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TeachersListViewHolder extends RecyclerView.ViewHolder {
    TextView title,subtitle;

    public TeachersListViewHolder(@NonNull View itemView) {
        super(itemView);
        title=(TextView)itemView.findViewById(R.id.teacherListRowTitle_textView);
        subtitle=(TextView)itemView.findViewById(R.id.teacherListSubtitle_textView);
    }
}
