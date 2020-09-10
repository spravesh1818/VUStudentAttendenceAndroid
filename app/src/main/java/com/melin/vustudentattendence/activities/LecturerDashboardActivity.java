package com.melin.vustudentattendence.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.melin.vustudentattendence.R;

public class LecturerDashboardActivity extends AppCompatActivity {

    private static CardView todaysClasses;
    private static CardView signOut;
    private static CardView studentInfo;
    private static CardView attendenceHistory;
    private static FirebaseAuth fAuth;
    private static AlertDialog.Builder builder;
    private String documentUrl="/VU/VUAdmin/Lecturer/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecuturerdashboard);
        final String intentData=getIntent().getExtras().getString("email");
        Toast.makeText(this,intentData,Toast.LENGTH_LONG).show();
        todaysClasses=(CardView)findViewById(R.id.todaysClasses);
        signOut=(CardView)findViewById(R.id.teacher_signout);
//        studentInfo=(CardView)findViewById(R.id.student_info);
//        attendenceHistory=(CardView)findViewById(R.id.attendence_history);
        fAuth=FirebaseAuth.getInstance();
        builder=new AlertDialog.Builder(this);
        todaysClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lecturerUrl=documentUrl+intentData+"/";
                Intent todayClasses=new Intent(LecturerDashboardActivity.this,LecutererClassesListActivity.class);
                todayClasses.putExtra("document",lecturerUrl);
                startActivity(todayClasses);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutAction();
            }
        });

//        studentInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(LecturerDashboardActivity.this,"Student Info",Toast.LENGTH_LONG).show();
//            }
//        });
//
//        attendenceHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(LecturerDashboardActivity.this,"Attendence history",Toast.LENGTH_LONG).show();
//            }
//        });




    }

    private void signOutAction(){
        builder.setTitle("Sign Out");
        builder.setMessage("Do you really want to Sign Out?").setCancelable(false).setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try{
                    fAuth.signOut();
                    Toast.makeText(LecturerDashboardActivity.this,"Successfully signed out",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(LecturerDashboardActivity.this,LoginActivity.class);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(LecturerDashboardActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        signOutAction();
    }
}