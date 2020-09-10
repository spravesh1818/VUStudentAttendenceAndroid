package com.melin.vustudentattendence;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LecturerClassListRowAdapter extends RecyclerView.Adapter<LecturerClassListViewHolder> {

    List<Classes> lecturerClassList;

    public LecturerClassListRowAdapter(List<Classes> lecturerClassListList) {
        this.lecturerClassList = lecturerClassListList;
    }

    @NonNull
    @Override
    public LecturerClassListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lecturer_row,parent,false);
        return new LecturerClassListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LecturerClassListViewHolder holder, int position) {
        final Classes classes=lecturerClassList.get(position);
        holder.subjectCode.setText(classes.getCode());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),ClassAttendenceActivity.class);
                intent.putExtra("document",classes.url);
                Log.d("Test","Switching to test activity");
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lecturerClassList.size();
    }
}
