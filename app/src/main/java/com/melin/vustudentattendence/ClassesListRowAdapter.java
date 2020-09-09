package com.melin.vustudentattendence;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClassesListRowAdapter extends RecyclerView.Adapter<ClassesListViewHolder> {

    private List<Classes> classesList;

    public ClassesListRowAdapter(List<Classes> classesList) {
        this.classesList = classesList;
    }

    @NonNull
    @Override
    public ClassesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.classes_list_row,parent,false);
        return new ClassesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassesListViewHolder holder, int position) {
        Classes classes=classesList.get(position);
        holder.title.setText(classes.getCode());
    }

    @Override
    public int getItemCount() {
        return classesList.size();
    }
}
