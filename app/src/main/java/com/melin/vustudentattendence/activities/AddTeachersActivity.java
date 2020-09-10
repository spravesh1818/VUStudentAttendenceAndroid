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
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.melin.vustudentattendence.R;
import com.melin.vustudentattendence.rowAdapter.TeachersListRowAdapter;
import com.melin.vustudentattendence.models.Lecturer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTeachersActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    private List<Lecturer> lecturerArrayList=new ArrayList<>();
    private FirebaseFirestore fs;
    TeachersListRowAdapter teachersListRowAdapter;
    FirebaseAuth firebaseAuth;
    FloatingActionButton floatingActionButton;
    AlertDialog.Builder builder;
    private static ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teachers);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.teacher_add_button);
        fs=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        builder=new AlertDialog.Builder(this);
        mRecyclerView=(RecyclerView)findViewById(R.id.teacherListRecyclerView);
        LinearLayoutManager layout=new LinearLayoutManager(AddTeachersActivity.this);
        mRecyclerView.setLayoutManager(layout);


        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Loading...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        Toolbar toolbar=(Toolbar)findViewById(R.id.add_teacher_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add lecturer");

        getAndPopulateView();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

    }

    private void showAddDialog(){
        builder.setTitle("Add Lecturer");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.add_teacher_form,null);

        final EditText name = (EditText) register_layout.findViewById(R.id.teacherName);
        final EditText department = (EditText) register_layout.findViewById(R.id.teacherDepartment);
        final EditText email = (EditText) register_layout.findViewById(R.id.teacherEmail);

        builder.setView(register_layout);



        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               if(name.getText().toString().equals("")){
                   Toast.makeText(AddTeachersActivity.this,"Name is required",Toast.LENGTH_LONG).show();
                   return;
               }
                if(department.getText().toString().equals("")){
                    Toast.makeText(AddTeachersActivity.this,"Department is required is required",Toast.LENGTH_LONG).show();
                    return;
                }
                if(email.getText().toString().equals("")){
                    Toast.makeText(AddTeachersActivity.this,"Email is required",Toast.LENGTH_LONG).show();
                    return;
                }

                Lecturer lecturer=new Lecturer();
                lecturer.setName(name.getText().toString());
                lecturer.setDepartment(department.getText().toString());
                lecturer.setEmail(email.getText().toString());
                addTeacher(lecturer);
            }
        });


        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }



    private void updateView(){
        teachersListRowAdapter=new TeachersListRowAdapter(lecturerArrayList);
        mRecyclerView.setAdapter(teachersListRowAdapter);
    }


    private void getAndPopulateView(){
        mProgress.show();
        fs.collection("/VU/VUAdmin/Lecturer").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mProgress.dismiss();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String,Object> data=document.getData();
                        Lecturer lecturer=new Lecturer();
                        lecturer.setEmail(document.getId().toString());
                        for (Map.Entry<String,Object> entry : data.entrySet()){
                            if(entry.getKey().equals("name")){
                                lecturer.setName(entry.getValue().toString());
                            }
                            if(entry.getKey().equals("department")){
                                lecturer.setDepartment(entry.getValue().toString());
                            }
                        }
                        lecturerArrayList.add(lecturer);
                        updateView();
                    }
                } else {
                    Log.d("List", "Error getting documents: ", task.getException());
                }
            }


        });;
    }

    private void addTeacher(final Lecturer lecturer){

        Map<String,Object> lecturerMap=new HashMap<>();
        lecturerMap.put("department",lecturer.getDepartment());
        lecturerMap.put("name",lecturer.getName());
        fs.collection("/VU/VUAdmin/Lecturer").document(lecturer.getEmail()).set(lecturerMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddTeachersActivity.this,"Lecturer Added Successfully",Toast.LENGTH_LONG).show();
                Log.d("Firebase", "DocumentSnapshot successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Firebase", "Error writing document", e);
            }
        });


        firebaseAuth.createUserWithEmailAndPassword(lecturer.getEmail(),"password123@").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mProgress.show();
                    String uid=task.getResult().getUser().getUid();
                    Map<String,Object> lecturerRole=new HashMap<>();
                    lecturerRole.put("role","lecturer");
                    fs.collection("/users/").document(uid).set(lecturerRole).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mProgress.dismiss();
                            Toast.makeText(AddTeachersActivity.this,"User for lecturer created successfully",Toast.LENGTH_LONG).show();
                            lecturerArrayList.add(lecturer);
                            updateView();
                            Log.d("Firebase","Successfully written to the database");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Log.d("Firebase","Successfully written to the database");
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });




    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}