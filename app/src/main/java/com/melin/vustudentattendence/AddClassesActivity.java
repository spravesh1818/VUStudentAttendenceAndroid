package com.melin.vustudentattendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddClassesActivity extends AppCompatActivity {

    RecyclerView classList;
    private List<Classes> classesList=new ArrayList<>();
    private List<Lecturer> lecturerList=new ArrayList<>();
    private FirebaseFirestore fs;
    ClassesListRowAdapter classesListRowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);
        fs=FirebaseFirestore.getInstance();



        classList=(RecyclerView)findViewById(R.id.classListRecyclerView);
        LinearLayoutManager layout=new LinearLayoutManager(AddClassesActivity.this);
        classList.setLayoutManager(layout);

        getClassesDataAndPopulateView();
        updateView();

    }

    private void updateView(){
        classesListRowAdapter=new ClassesListRowAdapter(classesList);
        classList.setAdapter(classesListRowAdapter);
    }


    private void getClassesDataAndPopulateView(){
        final String lecuturerUrl="/VU/VUAdmin/Lecturer";
        fs.collection(lecuturerUrl)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final String lecturerId=document.getId();
                                lecturerList.add(new Lecturer(lecturerId));
                                fs.collection(lecuturerUrl+"/"+lecturerId+"/classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Classes classes=new Classes(document.getId());
                                                classesList.add(classes);
                                            }
                                            classesListRowAdapter=new ClassesListRowAdapter(classesList);
                                            classList.setAdapter(classesListRowAdapter);

                                        } else {
                                            Log.d("List", "Error getting documents: ", task.getException());
                                        }
                                    }


                                });

                                Log.d("List", document.getId() + " => " + document.getData());
                            }



                        } else {
                            Log.d("List", "Error getting documents: ", task.getException());
                        }
                    }


                });


    }


    private void addNewClass(String lecturer,Classes classes){

    }



}