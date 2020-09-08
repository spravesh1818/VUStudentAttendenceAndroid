package com.melin.vustudentattendence;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class LecturerClassListViewHolder extends RecyclerView.ViewHolder {
    TextView subjectCode;
    Button button;


    public LecturerClassListViewHolder(@NonNull View itemView) {
        super(itemView);
        subjectCode=itemView.findViewById(R.id.title_textView);
        button=itemView.findViewById(R.id.attendenceButton);
    }
}
