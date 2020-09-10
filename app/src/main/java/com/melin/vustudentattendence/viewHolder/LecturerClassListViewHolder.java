package com.melin.vustudentattendence.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.melin.vustudentattendence.R;

public class LecturerClassListViewHolder extends RecyclerView.ViewHolder {
    TextView subjectCode;
    Button button;


    public TextView getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(TextView subjectCode) {
        this.subjectCode = subjectCode;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public LecturerClassListViewHolder(@NonNull View itemView) {
        super(itemView);
        subjectCode=itemView.findViewById(R.id.title_textView);
        button=itemView.findViewById(R.id.attendenceButton);
    }
}
