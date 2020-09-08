package com.melin.vustudentattendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ClassAttendenceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private  ClassAttendenceRowAdapter adapter;
    private List<Student> students=new ArrayList<>();
    private FirebaseFirestore fs;
    private String incomingIntentData;
    private NfcAdapter mNfcAdapter;
    private static String TAG="Attendence Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_attendence);


        //check nfc first
        mNfcAdapter=NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter==null){
            Toast.makeText(this,"This device doesn't support NFC",Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if(!mNfcAdapter.isEnabled()){
            Toast.makeText(this,"NFC is disabled.Please enable it",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "NFC is in reader mode", Toast.LENGTH_SHORT).show();
        }
        //incomingIntentData=getIntent().getExtras().getString("document");
        //fs=FirebaseFirestore.getInstance();
        //Toast.makeText(this,incomingIntentData,Toast.LENGTH_LONG).show();
        //recyclerView=(RecyclerView)findViewById(R.id.attendenceListRecyclerView);
//        LinearLayoutManager layoutManager=new LinearLayoutManager(ClassAttendenceActivity.this);
//        recyclerView.setLayoutManager(layoutManager);
//        populateStudentList(incomingIntentData);
        handleIntent(getIntent());
    }

    private void populateStudentList(String url){
        fs.collection(url+"/students")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> data=document.getData();
                                Student student=new Student();
                                student.setStatus("ABSENT");
                                for (Map.Entry<String,Object> entry : data.entrySet()){
                                    if(entry.getKey().equals("name")){
                                        student.setName(entry.getValue().toString());
                                    }
                                    if(entry.getKey().equals("student_id")){
                                        student.setStudent_id(entry.getValue().toString());
                                    }
                                }
                                students.add(student);
                                Log.d("List", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("List", "Error getting documents: ", task.getException());
                        }

                        adapter=new ClassAttendenceRowAdapter(students);
                        recyclerView.setAdapter(adapter);

                    }


                });

    }

    public void simulatingAnIntent(){
        String student_id="101";
        for(Student student:students){
            if(student.getStudent_id().equals(student_id)){
                student.setStatus("PRESENT");
                Toast.makeText(ClassAttendenceActivity.this,"Student ID"+student.getStudent_id()+"Detected",Toast.LENGTH_LONG).show();
            }
        }

        adapter=new ClassAttendenceRowAdapter(students);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this,mNfcAdapter);
        super.onPause();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(ClipDescription.MIMETYPE_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }


    private void handleIntent(Intent intent){
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (ClipDescription.MIMETYPE_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }


    public class NdefReaderTask extends AsyncTask<Tag,Void,String> {
        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
            /*
             * See NFC forum specification for "Text Record Type Definition" at 3.2.1
             *
             * http://www.nfc-forum.org/specs/
             *
             * bit_7 defines encoding
             * bit_6 reserved for future use, must be 0
             * bit_5..0 length of IANA language code
             */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {

                Toast.makeText(ClassAttendenceActivity.this,"Student ID"+result+"Detected",Toast.LENGTH_LONG).show();

//                for(Student student:students){
//                    if(student.getStudent_id().equals(result)){
//                        student.setStatus("PRESENT");
//                        Toast.makeText(ClassAttendenceActivity.this,"Student ID"+student.getStudent_id()+"Detected",Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                adapter=new ClassAttendenceRowAdapter(students);
//                recyclerView.setAdapter(adapter);
            }
        }
    }
}