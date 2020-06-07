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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class memoActivity extends AppCompatActivity {

    private static final String TAG = memoActivity.class.getSimpleName();
    public static final int REQUEST_CODE_NEW_MEMO = 1000;
    private static final int REQUEST_CODE_RENEW_MEMO = 2000;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference database;

    private static HashMap<Integer, String> memoListMapper = new HashMap<Integer, String>();

    private List<Memo> mMemoList = new ArrayList<>();;
    private MemoAdapter mAdapter;
    private ListView mMemoListView;
    private static int memoNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDB = FirebaseDatabase.getInstance();

        mMemoListView = (ListView) findViewById(R.id.memo_list);

        mMemoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memo memo = mMemoList.get(position);
                memoNum = position;
                Bundle bundle = new Bundle();
                bundle.putString("key",memo.getKey());
                bundle.putString("title",memo.getTitle());
                bundle.putString("content", memo.getContent());
                Intent intent = new Intent(memoActivity.this, MemoEditActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_RENEW_MEMO);
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
                Intent intent = new Intent(memoActivity.this, MemoEditActivity.class);
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
        database = FirebaseDatabase.getInstance().getReference("memo"+mFirebaseAuth.getUid());
       database.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               Memo memo = dataSnapshot.getValue(Memo.class);
               memo.setKey(dataSnapshot.getKey());
               mMemoList.add(memo);
               memoListMapper.put(memoNum, dataSnapshot.getKey());
               mAdapter.notifyDataSetChanged();
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               Memo memo = dataSnapshot.getValue(Memo.class);
               memo.setKey(dataSnapshot.getKey());

               mMemoList.remove(memoNum);
               mMemoList.add(memoNum, memo);

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
                memo.setKey(key);
            }
            else {
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show();
            }
        }if(requestCode == REQUEST_CODE_RENEW_MEMO) {
            if (resultCode == RESULT_OK) {
                String key = data.getStringExtra("key");
                String title = data.getStringExtra("title");
                String content = data.getStringExtra("content");
                database = mFirebaseDB.getReference("memo" + mFirebaseAuth.getUid()).child(key);
                Map<String, Object> renew = new HashMap<String, Object>();
                renew.put("title", title);
                renew.put("content", content);
                database.updateChildren(renew);
                mAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
