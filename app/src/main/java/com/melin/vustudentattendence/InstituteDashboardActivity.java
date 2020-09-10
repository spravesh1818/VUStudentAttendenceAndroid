package com.melin.vustudentattendence;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class InstituteDashboardActivity extends AppCompatActivity{

    private CardView addLecturers;
    private CardView addClasses;
    private CardView addStudents;
    private CardView adminSignOut;
    private static FirebaseAuth fAuth;
    private static AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_dashboard);

        fAuth=FirebaseAuth.getInstance();
        addLecturers=(CardView)findViewById(R.id.add_teacher);
        addClasses=(CardView)findViewById(R.id.add_classes);
        addStudents=(CardView)findViewById(R.id.add_students);
        adminSignOut=(CardView)findViewById(R.id.admin_signout);
        builder=new AlertDialog.Builder(this);

        addLecturers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InstituteDashboardActivity.this,AddTeachersActivity.class);
                startActivity(intent);
            }
        });


        addClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InstituteDashboardActivity.this,AddClassesActivity.class);
                startActivity(intent);
            }
        });


        addStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InstituteDashboardActivity.this,AddStudentsActivity.class);
                startActivity(intent);
            }
        });


        adminSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutAction();
            }
        });

    }

    private void signOutAction(){
        builder.setTitle("Sign Out");
        builder.setMessage("Do you really want to Sign Out?").setCancelable(false).setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try{
                    fAuth.signOut();
                    Toast.makeText(InstituteDashboardActivity.this,"Successfully signed out",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(InstituteDashboardActivity.this,LoginActivity.class);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(InstituteDashboardActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
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