package com.melin.vustudentattendence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddStudentsActivity extends AppCompatActivity {


    RecyclerView mRecyclerView;
    private List<Student> studentList=new ArrayList<>();
    private FirebaseFirestore fs;
    StudentListRowAdapter studentListRowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students);


        fs=FirebaseFirestore.getInstance();

        studentList.add(new Student("test","test","test"));

        mRecyclerView=(RecyclerView)findViewById(R.id.studentListRecyclerView);
        LinearLayoutManager layout=new LinearLayoutManager(AddStudentsActivity.this);
        mRecyclerView.setLayoutManager(layout);

        studentListRowAdapter=new StudentListRowAdapter(studentList);
        mRecyclerView.setAdapter(studentListRowAdapter);

    }


    private void getAndPopulateRecyclerView(){
        //TODO:Get data from firebase and populate the view.
    }

    private void addStudentToClasses(String classesName,String lecturerName,Student student){

    }
}