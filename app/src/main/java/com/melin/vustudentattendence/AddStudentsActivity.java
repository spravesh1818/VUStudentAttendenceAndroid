package com.melin.vustudentattendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


        mRecyclerView=(RecyclerView)findViewById(R.id.studentListRecyclerView);
        LinearLayoutManager layout=new LinearLayoutManager(AddStudentsActivity.this);
        mRecyclerView.setLayoutManager(layout);

        studentListRowAdapter=new StudentListRowAdapter(studentList);
        mRecyclerView.setAdapter(studentListRowAdapter);

        getAndPopulateRecyclerView();
        updateView();
    }


    private void updateView(){
        studentListRowAdapter=new StudentListRowAdapter(studentList);
        mRecyclerView.setAdapter(studentListRowAdapter);
    }


    private void getAndPopulateRecyclerView(){
        final String lecturerPath="/VU/VUAdmin/Lecturer";
        fs.collection("/VU/VUAdmin/Lecturer").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(final QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        final String userId=documentSnapshot.getId();
                        fs.collection(lecturerPath+"/"+userId+"/classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                                        final String classesId=snapshot.getId();
                                        fs.collection(lecturerPath+"/"+userId+"/classes"+"/"+classesId+"/students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    for (QueryDocumentSnapshot studentSnapshot:task.getResult()){
                                                        Student student=new Student();
                                                        Map<String,Object> data=documentSnapshot.getData();
                                                        student.setName(data.get("name").toString());
                                                        student.setStudent_id(classesId);
                                                        studentList.add(student);
                                                        updateView();
                                                    }

                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }

                }
            }
        });

    }

    private void addStudentToClasses(String classesName,String lecturerName,Student student){

    }
}