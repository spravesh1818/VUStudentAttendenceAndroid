package com.melin.vustudentattendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LecutererClassesListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerview;
    LecturerClassListRowAdapter lecturerRowAdapter;
    private FirebaseFirestore fs;
    private List<Classes> lecturerClassesList=new ArrayList<>();
    private String incomingIntentData;
    private String classBaseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecuterer_list);
        fs=FirebaseFirestore.getInstance();
        incomingIntentData=getIntent().getExtras().getString("document");
        classBaseUrl=incomingIntentData+"classes";

        try{
            getDataAndPopulateView(classBaseUrl);
        }catch (Exception e){
            e.printStackTrace();
        }

        mRecyclerview=(RecyclerView)findViewById(R.id.lecturerlistrecyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(LecutererClassesListActivity.this);
        mRecyclerview.setLayoutManager(layoutManager);
    }

    private void getDataAndPopulateView(String url){
        fs.collection(url)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> data=document.getData();
                                for (Map.Entry<String,Object> entry : data.entrySet()){
                                    Classes capturedClasses=new Classes(entry.getValue().toString(),classBaseUrl+"/"+entry.getValue());
                                    lecturerClassesList.add(capturedClasses);
                                }

                                lecturerRowAdapter=new LecturerClassListRowAdapter(lecturerClassesList);

                                mRecyclerview.setAdapter(lecturerRowAdapter);
                                Log.d("List", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("List", "Error getting documents: ", task.getException());
                        }
                    }


                });


    }
}