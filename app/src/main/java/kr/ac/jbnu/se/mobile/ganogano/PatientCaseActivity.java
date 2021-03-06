package kr.ac.jbnu.se.mobile.ganogano;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

public class PatientCaseActivity extends AppCompatActivity {

    private static final String TAG = PatientCaseActivity.class.getSimpleName();
    public static final int REQUEST_CODE_NEW_CASE = 1000;
    private static final int REQUEST_CODE_RENEW_CASE = 2000;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference database;

    private static HashMap<Integer, String> caseListMapper = new HashMap<Integer, String>();
    private static int caseNum = 0;

    private List<PatientCase> mPatientCaseList = new ArrayList<>();
    ;
    private PatientCaseAdapter mAdapter;
    private ListView mPatientCaseListView;

    private String parentKey;
    private String parentHospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Bundle b = getIntent().getExtras();
        parentKey = b.getString("parentKey");
        parentHospital = b.getString("parentHospital");

        setTitle(parentHospital + " 환자케이스");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDB = FirebaseDatabase.getInstance();

        mPatientCaseListView = (ListView) findViewById(R.id.memo_list);

        mPatientCaseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PatientCase patientCase = mPatientCaseList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("key", patientCase.getKey());
                bundle.putString("sickness", patientCase.getSickness());
                bundle.putString("prescription", patientCase.getPrescription());
                bundle.putString("precaution", patientCase.getPrecaution());
                bundle.putString("etc", patientCase.getEtc());
                Intent intent = new Intent(PatientCaseActivity.this, PatientCaseEditActivity.class);
                intent.putExtras(bundle);
                caseNum = position;
                startActivityForResult(intent, REQUEST_CODE_RENEW_CASE);
            }
        });
        mPatientCaseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("주의")
                        .setMessage("정말 삭제하시겠습니까?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PatientCase patientCase = mPatientCaseList.get(pos);
                                mPatientCaseList.remove(pos);
                                mAdapter.notifyDataSetChanged();
                                String key = patientCase.getKey();
                                mFirebaseDB.getReference("practice" + mFirebaseAuth.getUid()).child(parentKey).child("case").child(key).removeValue();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
                return true;
            }

        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("key", null);
                Intent intent = new Intent(PatientCaseActivity.this, PatientCaseEditActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_NEW_CASE);
            }
        });
        //어댑터
        mAdapter = new PatientCaseAdapter(mPatientCaseList);
        mPatientCaseListView.setAdapter(mAdapter);
        initDatabase();
    }

    private void initDatabase() {
        database = FirebaseDatabase.getInstance().getReference("practice" + mFirebaseAuth.getUid()).child(parentKey).child("case");
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PatientCase patientCase = dataSnapshot.getValue(PatientCase.class);
                patientCase.setKey(dataSnapshot.getKey());
                mPatientCaseList.add(patientCase);
                caseListMapper.put(caseNum, dataSnapshot.getKey());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PatientCase patientCase = dataSnapshot.getValue(PatientCase.class);
                patientCase.setKey(dataSnapshot.getKey());

                mPatientCaseList.remove(caseNum);
                mPatientCaseList.add(caseNum, patientCase);

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }


            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_NEW_CASE) {
            if (resultCode == RESULT_OK) {
                String sickness = data.getStringExtra("sickness");
                String prescription = data.getStringExtra("prescription");
                String precaution = data.getStringExtra("precaution");
                String etc = data.getStringExtra("etc");
                String key = data.getStringExtra("key");
                PatientCase patientCase = new PatientCase(sickness, prescription, precaution, etc, key);
                patientCase.setSickness(sickness);
                patientCase.setPrescription(prescription);
                patientCase.setPrecaution(precaution);
                patientCase.setEtc(etc);
                String id = mFirebaseAuth.getUid();
                database = mFirebaseDB.getReference("practice" + id).child(parentKey).child("case");
                patientCase.setKey(database.push().getKey());
                database.push().setValue(patientCase);
                patientCase.setKey(key);
            } else {
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == REQUEST_CODE_RENEW_CASE) {
            if (resultCode == RESULT_OK) {
                String sickness = data.getStringExtra("sickness");
                String prescription = data.getStringExtra("prescription");
                String precaution = data.getStringExtra("precaution");
                String etc = data.getStringExtra("etc");
                String key = data.getStringExtra("key");
                database = mFirebaseDB.getReference("practice" + mFirebaseAuth.getUid()).child(parentKey).child("case").child(key);
                Map<String, Object> renew = new HashMap<String, Object>();
                renew.put("sickness", sickness);
                renew.put("prescription", prescription);
                renew.put("precaution", precaution);
                renew.put("etc", etc);
                database.updateChildren(renew);
                mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
}

