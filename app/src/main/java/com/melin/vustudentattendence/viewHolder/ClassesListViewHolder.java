package com.melin.vustudentattendence.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.melin.vustudentattendence.R;

public class ClassesListViewHolder extends RecyclerView.ViewHolder {
    TextView title,subtitle;

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(TextView subtitle) {
        this.subtitle = subtitle;
    }

    public ClassesListViewHolder(@NonNull View itemView) {
        super(itemView);
        title=(TextView)itemView.findViewById(R.id.classesListTitle_textView);
        subtitle=(TextView)itemView.findViewById(R.id.classesListSubtitle_textView);
    }
}
