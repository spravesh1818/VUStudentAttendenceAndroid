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
import com.melin.vustudentattendence.rowAdapter.ClassesListRowAdapter;
import com.melin.vustudentattendence.R;
import com.melin.vustudentattendence.models.Classes;
import com.melin.vustudentattendence.models.Lecturer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddClassesActivity extends AppCompatActivity {

    RecyclerView classList;
    private List<Classes> classesList=new ArrayList<>();
    private List<Lecturer> lecturerList=new ArrayList<>();
    private FirebaseFirestore fs;
    private List<String> lecturerForSelect=new ArrayList<>();
    ClassesListRowAdapter classesListRowAdapter;
    FloatingActionButton floatingActionButton;
    AlertDialog.Builder builder;
    Spinner lecturerSpinner;
    private static ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);
        fs=FirebaseFirestore.getInstance();

        builder=new AlertDialog.Builder(this);


        classList=(RecyclerView)findViewById(R.id.classListRecyclerView);
        LinearLayoutManager layout=new LinearLayoutManager(AddClassesActivity.this);
        classList.setLayoutManager(layout);


        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Loading...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        getClassesDataAndPopulateView();
        floatingActionButton=(FloatingActionButton)findViewById(R.id.class_add_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


        Toolbar toolbar=(Toolbar)findViewById(R.id.add_classes_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Classes");


    }

    private void updateView(){
        classesListRowAdapter=new ClassesListRowAdapter(classesList);
        classList.setAdapter(classesListRowAdapter);
    }


    private void getClassesDataAndPopulateView(){
        mProgress.show();
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
                                lecturerForSelect.add(lecturerId);
                                fs.collection(lecuturerUrl+"/"+lecturerId+"/classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            mProgress.dismiss();
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Classes classes=new Classes(document.getId());
                                                classesList.add(classes);
                                                updateView();
                                            }

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


    private void addNewClass(String lecturer, final Classes classes){
        mProgress.show();
        String baseUrl="/VU/VUAdmin/Lecturer/";
        String url=baseUrl+lecturer+"/classes/";
        HashMap<String,Object> inputDocument=new HashMap<>();
        inputDocument.put("code",classes.getCode());
        fs.collection(url).document(classes.getCode()).set(inputDocument).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                classesList.add(classes);
                updateView();
                mProgress.dismiss();
                Toast.makeText(AddClassesActivity.this,"Class added successfully",Toast.LENGTH_LONG).show();
                Log.d("Firebase","Successfully added document");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgress.dismiss();
                Toast.makeText(AddClassesActivity.this,"Failed to add class",Toast.LENGTH_LONG).show();
                Log.d("Firebase","Failed to write document");
            }
        });
    }


    private void showDialog(){
        builder.setTitle("Add Class");
        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.add_classes_form,null);

        final EditText name = (EditText) register_layout.findViewById(R.id.className);
        lecturerSpinner = (Spinner) register_layout.findViewById(R.id.selectLecturerForClasses);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,lecturerForSelect);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lecturerSpinner.setAdapter(arrayAdapter);

        builder.setView(register_layout);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(name.getText().toString().equals("")){
                    Toast.makeText(AddClassesActivity.this,"Name field cannot be empty",Toast.LENGTH_LONG).show();
                    return;
                }

                String lecturerName=lecturerSpinner.getSelectedItem().toString();
                Classes classes=new Classes(name.getText().toString());
                addNewClass(lecturerName,classes);
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}