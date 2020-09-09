package com.melin.vustudentattendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teachers);

        fs=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        mRecyclerView=(RecyclerView)findViewById(R.id.teacherListRecyclerView);
        LinearLayoutManager layout=new LinearLayoutManager(AddTeachersActivity.this);
        mRecyclerView.setLayoutManager(layout);


        getAndPopulateView();
        updateView();

    }



    private void updateView(){
        teachersListRowAdapter=new TeachersListRowAdapter(lecturerArrayList);
        mRecyclerView.setAdapter(teachersListRowAdapter);
    }


    private void getAndPopulateView(){
        fs.collection("/VU/VUAdmin/Lecturer").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
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
                    }
                } else {
                    Log.d("List", "Error getting documents: ", task.getException());
                }
            }


        });;
    }

    private void addTeacher(Lecturer lecturer){
        Map<String,Object> lecturerMap=new HashMap<>();
        lecturerMap.put("department",lecturer.department);
        lecturerMap.put("name",lecturer.name);
        fs.collection("/VU/VUAdmin/Lecturer").document(lecturer.email).set(lecturerMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Firebase", "DocumentSnapshot successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Firebase", "Error writing document", e);
            }
        });


        firebaseAuth.createUserWithEmailAndPassword(lecturer.email,"password123@").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Map<String,Object> lecturerRole=new HashMap<>();
                    lecturerRole.put("role","lecturer");
                    String uid=task.getResult().getUser().getUid();

                    fs.collection("users").document(uid).set(lecturerRole).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Firebase","Successfully written to the database");
                        }
                    });
                }
            }
        });
    }
}