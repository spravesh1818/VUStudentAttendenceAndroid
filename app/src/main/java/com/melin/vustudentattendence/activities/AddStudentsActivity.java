package com.melin.vustudentattendence.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.melin.vustudentattendence.R;
import com.melin.vustudentattendence.rowAdapter.StudentListRowAdapter;
import com.melin.vustudentattendence.models.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddStudentsActivity extends AppCompatActivity {


    RecyclerView mRecyclerView;
    private List<Student> studentList=new ArrayList<>();
    private List<String> lecturerList=new ArrayList<>();
    private FirebaseFirestore fs;
    StudentListRowAdapter studentListRowAdapter;
    private List<String> classesList=new ArrayList<>();
    FloatingActionButton floatingActionButton;
    AlertDialog.Builder builder;
    Spinner lecturerSpinner,classSpinner;
    private static ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students);


        fs=FirebaseFirestore.getInstance();

        builder=new AlertDialog.Builder(this);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.student_add_button);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });



        Toolbar toolbar=(Toolbar)findViewById(R.id.add_student_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Students");

        mRecyclerView=(RecyclerView)findViewById(R.id.studentListRecyclerView);
        LinearLayoutManager layout=new LinearLayoutManager(AddStudentsActivity.this);
        mRecyclerView.setLayoutManager(layout);

        studentListRowAdapter=new StudentListRowAdapter(studentList);
        mRecyclerView.setAdapter(studentListRowAdapter);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Loading...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        getAndPopulateRecyclerView();
    }


    private void updateView(){
        studentListRowAdapter=new StudentListRowAdapter(studentList);
        mRecyclerView.setAdapter(studentListRowAdapter);
    }


    private void getAndPopulateRecyclerView(){
        mProgress.show();
        final String lecturerPath="/VU/VUAdmin/Lecturer";
        fs.collection("/VU/VUAdmin/Lecturer").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(final QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        final String userId=documentSnapshot.getId();
                        lecturerList.add(userId);
                        fs.collection(lecturerPath+"/"+userId+"/classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    mProgress.dismiss();
                                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                                        final String classesId=snapshot.getId();
                                        classesList.add(classesId);
                                        fs.collection(lecturerPath+"/"+userId+"/classes"+"/"+classesId+"/students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    for (QueryDocumentSnapshot studentSnapshot:task.getResult()){
                                                        Student student=new Student();
                                                        Map<String,Object> data=studentSnapshot.getData();
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


    private void showDialog(){
        builder.setTitle("Add Class");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.add_student_form,null);

        final EditText name = (EditText) register_layout.findViewById(R.id.studentName);
        final EditText student_id = (EditText) register_layout.findViewById(R.id.student_add_student_id);



        lecturerSpinner = (Spinner) register_layout.findViewById(R.id.student_add_lecturerSelect);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,lecturerList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lecturerSpinner.setAdapter(arrayAdapter);

        classSpinner = (Spinner) register_layout.findViewById(R.id.student_add_class_select);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,classesList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);

        builder.setView(register_layout);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(student_id.getText().toString().equals("")){
                    Toast.makeText(AddStudentsActivity.this,"Student id cannot be blank",Toast.LENGTH_LONG);
                    return;
                }

                if(name.getText().toString().equals("")){
                    Toast.makeText(AddStudentsActivity.this,"Name cannot be blank",Toast.LENGTH_LONG);
                    return;
                }

                addStudentToClasses(classSpinner.getSelectedItem().toString(),lecturerSpinner.getSelectedItem().toString(),new Student(name.getText().toString(),student_id.getText().toString(),"Absent"));

            }
        });


        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }

    private void addStudentToClasses(String classesName, String lecturerName, final Student student){
        mProgress.show();
        String baseUrl="/VU/VUAdmin/Lecturer/";
        String url=baseUrl+lecturerName+"/classes/"+classesName+"/students/";

        HashMap<String,Object> inputDocument=new HashMap<>();
        inputDocument.put("name",student.getName());
        inputDocument.put("student_id",student.getStudent_id());
        fs.collection(url).document(student.getStudent_id()).set(inputDocument).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                studentList.add(student);
                updateView();
                Toast.makeText(AddStudentsActivity.this,"Student Added Successfully",Toast.LENGTH_LONG).show();
                mProgress.dismiss();
                Log.d("Firebase","Data added successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgress.dismiss();
                Toast.makeText(AddStudentsActivity.this,"Failed to add student",Toast.LENGTH_LONG).show();
                Log.d("Firebase","  Could not insert data");
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}