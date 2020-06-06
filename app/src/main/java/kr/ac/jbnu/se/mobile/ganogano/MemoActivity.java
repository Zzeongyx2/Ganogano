package kr.ac.jbnu.se.mobile.ganogano;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MemoActivity extends AppCompatActivity {

    private static final String TAG = MemoActivity.class.getSimpleName();
    public static final int REQUEST_CODE_NEW_MEMO = 1000;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference database;

    private List<Memo> mMemoList;
    private MemoAdapter mAdapter;
    private ListView mMemoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDB = FirebaseDatabase.getInstance();

        mMemoListView = (ListView) findViewById(R.id.memo_list);
        mMemoList = new ArrayList<>();

        mMemoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memo memo = mMemoList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("key", memo.getKey());
                bundle.putString("title", memo.getTitle());
                bundle.putString("content", memo.getContent());
                Intent intent = new Intent(MemoActivity.this, MemoEditActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_NEW_MEMO);
                mMemoList.remove(position);
                mAdapter.notifyDataSetChanged();
                String key = memo.getKey();
                mFirebaseDB.getReference("memo" + mFirebaseAuth.getUid()).child(key).removeValue();
            }
        });
        mMemoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("주의")
                        .setMessage("정말 삭제하시겠습니까?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Memo memo = mMemoList.get(pos);
                                mMemoList.remove(pos);
                                mAdapter.notifyDataSetChanged();
                                String key = memo.getKey();
                                mFirebaseDB.getReference("memo" + mFirebaseAuth.getUid()).child(key).removeValue();
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
                Intent intent = new Intent(MemoActivity.this, MemoEditActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_NEW_MEMO);
            }
        });
        //어댑터
        mAdapter = new MemoAdapter(mMemoList);
        mMemoListView.setAdapter(mAdapter);
        initDatabase();
    }

    private void initDatabase() {
        database = FirebaseDatabase.getInstance().getReference("memo" + mFirebaseAuth.getUid());
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Memo memo = dataSnapshot.getValue(Memo.class);
                memo.setKey(dataSnapshot.getKey());
                mMemoList.add(memo);
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
        if (requestCode == REQUEST_CODE_NEW_MEMO) {
            if (resultCode == RESULT_OK) {
                String title = data.getStringExtra("title");
                String content = data.getStringExtra("content");
                String key = data.getStringExtra("key");
                Memo memo = new Memo(title, content, key);
                memo.setTitle(title);
                memo.setContent(content);
                String id = mFirebaseAuth.getUid();
                database = mFirebaseDB.getReference("memo" + id);
                memo.setKey(database.push().getKey());
                database.push().setValue(memo);
//                Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_LONG).show();
                memo.setKey(key);
            } else {
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }


}
