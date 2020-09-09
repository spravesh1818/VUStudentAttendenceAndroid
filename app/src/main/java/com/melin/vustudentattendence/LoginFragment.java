package com.melin.vustudentattendence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;


public class LoginFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static EditText username;
    private static EditText password;
    private static Button login_btn;
    private static ProgressDialog mProgress;
    private static String TAG="LoginFragment";
    private FirebaseFirestore fs;
    View view;
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;

    public LoginFragment() {
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(getActivity());
        mProgress.setTitle("Authenticating...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_login, container, false);
        username=(EditText)view.findViewById(R.id.et_email);
        password=(EditText)view.findViewById(R.id.et_password);
        login_btn=(Button)view.findViewById(R.id.btn_login);
        login_btn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        mProgress.show();
        login_btn.setEnabled(false);
        if(username.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Username cannot be empty",
                    Toast.LENGTH_SHORT).show();
            login_btn.setEnabled(true);
            return;
        }
        if(password.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Password cannot be empty",
                    Toast.LENGTH_SHORT).show();
            login_btn.setEnabled(true);
            return;
        }

        signIn(username.getText().toString(),password.getText().toString());

    }

    private void signIn(final String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mProgress.dismiss();
                    final String uid=task.getResult().getUser().getUid();
                    Log.d("Sign In","Sucessfully logged in");
                    Toast.makeText(getActivity(), "Authentication successful.",
                            Toast.LENGTH_SHORT).show();
                    fs=FirebaseFirestore.getInstance();
                    Log.d("Firebase instance", fs.getClass().toString());
                    //update ui accordingly
                    fs.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String,Object> data=document.getData();
                                    Log.d("Get user uid",document.getId());
                                    Log.d("Firebase instance",data.get("role").toString());
                                    if(data.get("role").toString().equals("admin") && uid.equals(document.getId().toString())){
                                        Intent homeActivity = new Intent(getActivity(), InstituteDashboardActivity.class);
                                        homeActivity.putExtra("email",email);
                                        startActivity(homeActivity);
                                    }else if(data.get("role").toString().equals("lecturer") && uid.equals(document.getId().toString())){
                                        Intent homeActivity = new Intent(getActivity(), LecturerDashboardActivity.class);
                                        homeActivity.putExtra("email",email);
                                        startActivity(homeActivity);
                                    }else{
                                        Log.d("Firestore instance","Student Login");
                                        Toast.makeText(getActivity(), "Student Login",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });

                } else {
                    mProgress.dismiss();
                    login_btn.setEnabled(true);
                    Log.w("Sign In", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(getActivity(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}