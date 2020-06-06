package kr.ac.jbnu.se.mobile.ganogano;

import android.app.AlertDialog;
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

public class PracticeActivity extends AppCompatActivity {

    //content_memo랑 menu_memoedit 이름 변경해서 재사용

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_CODE_NEW_PRACTICE = 1000;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference database;

    SharedPreferences sf; //이미 저장된 값 가져오기

    private List<Practice> mPracticeList = new ArrayList<>();
    ;
    private PracticeAdapter mAdapter;
    private ListView mPracticeListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDB = FirebaseDatabase.getInstance();

        mPracticeListView = (ListView) findViewById(R.id.memo_list);

        mPracticeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("원하는 작업을 선택해 주세요");
                builder.setItems(R.array.LAN, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            sf = getSharedPreferences("settings", MODE_PRIVATE);
                            Practice practice = mPracticeList.get(position);
                            Bundle bundle = new Bundle();
                            bundle.putString("key", practice.getKey());
                            bundle.putString("period", practice.getPeriod());
                            bundle.putString("hospital", practice.getHospital());
                            Intent intent = new Intent(PracticeActivity.this, PracticeEditActivity.class);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, REQUEST_CODE_NEW_PRACTICE);
                            mPracticeList.remove(position);
                            mAdapter.notifyDataSetChanged();
                            String key = practice.getKey();
                            mFirebaseDB.getReference("practice" + mFirebaseAuth.getUid()).child(key).child("hospital").removeValue();
                            mFirebaseDB.getReference("practice" + mFirebaseAuth.getUid()).child(key).child("period").removeValue();
                        } else {
                            Practice practice = mPracticeList.get(position);
                            mPracticeList.remove(position);
                            mAdapter.notifyDataSetChanged();
                            String key = practice.getKey();
                            mFirebaseDB.getReference("practice" + mFirebaseAuth.getUid()).child(key).removeValue();
                        }
                    }
                });
                builder.show();
                return false;
            }
        });

        mPracticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Practice practice = mPracticeList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("parentKey", practice.getKey());
                Intent I = new Intent(PracticeActivity.this, PatientCaseActivity.class);
                I.putExtras(bundle);
                startActivity(I);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("key", null);
                Intent intent = new Intent(PracticeActivity.this, PracticeEditActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_NEW_PRACTICE);
            }
        });
        //어댑터
        mAdapter = new PracticeAdapter(mPracticeList);
        mPracticeListView.setAdapter(mAdapter);
        initDatabase();
    }

    private void initDatabase() {
        database = FirebaseDatabase.getInstance().getReference("practice" + mFirebaseAuth.getUid());
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Practice practice = dataSnapshot.getValue(Practice.class);
                practice.setKey(dataSnapshot.getKey());
                mPracticeList.add(practice);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
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

        if (requestCode == REQUEST_CODE_NEW_PRACTICE) {
            if (resultCode == RESULT_OK) {
                String period = data.getStringExtra("period");
                String hospital = data.getStringExtra("hospital");
                String key = data.getStringExtra("key");
                Practice practice = new Practice(period, hospital, key);
                practice.setPeriod(period);
                practice.setHospital(hospital);
                String id = mFirebaseAuth.getUid();
                database = mFirebaseDB.getReference("practice" + id);
                practice.setKey(database.push().getKey());
                database.push().setValue(practice);
//                Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_LONG).show();
//                practice.setKey(key);
            } else {
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
