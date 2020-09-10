package com.melin.vustudentattendence.rowAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.melin.vustudentattendence.R;
import com.melin.vustudentattendence.viewHolder.TeachersListViewHolder;
import com.melin.vustudentattendence.models.Lecturer;

import java.util.List;

public class TeachersListRowAdapter extends RecyclerView.Adapter<TeachersListViewHolder> {

    private List<Lecturer> lecturerList;

    public TeachersListRowAdapter(List<Lecturer> lecturerList) {
        this.lecturerList = lecturerList;
    }

    @NonNull
    @Override
    public TeachersListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.acitivity_teachers_list_row,parent,false);
        return new TeachersListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeachersListViewHolder holder, int position) {
        Lecturer lecturer=lecturerList.get(position);
        holder.getTitle().setText(lecturer.getName());
        holder.getSubtitle().setText(lecturer.getEmail());
    }

    @Override
    public int getItemCount() {
        return lecturerList.size();
    }
}
