package com.melin.vustudentattendence;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClassAttendenceRowAdapter extends RecyclerView.Adapter<ClassAttendenceViewHolder> {

    private List<Student> studentList;

    public ClassAttendenceRowAdapter(List<Student> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ClassAttendenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_student_row,parent,false);
        return new ClassAttendenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassAttendenceViewHolder holder, int position) {
        Student student=studentList.get(position);
        holder.name.setText(student.getName());
        holder.student_id.setText(student.getStudent_id());
        holder.status.setText(student.getStatus());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}
