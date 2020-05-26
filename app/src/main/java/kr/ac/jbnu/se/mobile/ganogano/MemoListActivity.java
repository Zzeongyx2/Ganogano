package kr.ac.jbnu.se.mobile.ganogano;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MemoListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memolist);

        final ArrayList<String> items = new ArrayList<String>();
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, items);
        final ListView listview = (ListView) findViewById(R.id.memo_list);
        listview.setAdapter(adapter);

//        Button addButton = (Button)findViewById(R.id.add_memo);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int count;
//                count = adapter.getCount();
//                items.add("List" + Integer.toString(count+1));
//                adapter.notifyDataSetChanged();
//            }
//        });
    }
    public void add_memo(View o) {
        Intent intent = new Intent(this, MemoActivity.class);
        startActivity(intent);
    }
}
