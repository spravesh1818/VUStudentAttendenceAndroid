package com.melin.vustudentattendence;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClassAttendenceViewHolder extends RecyclerView.ViewHolder {
    TextView name,student_id,status;

    public ClassAttendenceViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.class_student_name);
        student_id=itemView.findViewById(R.id.class_student_id);
        status=itemView.findViewById(R.id.class_student_status);
    }
}
