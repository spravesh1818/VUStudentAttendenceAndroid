package com.melin.vustudentattendence.rowAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.melin.vustudentattendence.R;
import com.melin.vustudentattendence.viewHolder.StudentListViewHolder;
import com.melin.vustudentattendence.models.Student;

import java.util.List;

public class StudentListRowAdapter extends RecyclerView.Adapter<StudentListViewHolder> {

    List<Student> studentList;


    public StudentListRowAdapter(List<Student> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_row,parent,false);
        return new StudentListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListViewHolder holder, int position) {
        Student student=studentList.get(position);
        holder.getTitle().setText(student.getName());
        holder.getSubtitle().setText(student.getStudent_id());

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}
