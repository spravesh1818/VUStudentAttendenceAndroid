package com.melin.vustudentattendence.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.melin.vustudentattendence.R;

public class ClassAttendenceViewHolder extends RecyclerView.ViewHolder {
    TextView name,student_id,status;


    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getStudent_id() {
        return student_id;
    }

    public void setStudent_id(TextView student_id) {
        this.student_id = student_id;
    }

    public TextView getStatus() {
        return status;
    }

    public void setStatus(TextView status) {
        this.status = status;
    }

    public ClassAttendenceViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.class_student_name);
        student_id=itemView.findViewById(R.id.class_student_id);
        status=itemView.findViewById(R.id.class_student_status);
    }
}
