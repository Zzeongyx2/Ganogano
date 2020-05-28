package kr.ac.jbnu.se.mobile.ganogano;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends AppCompatActivity {
    //  RecyclerView recyclerView;
    List<Data> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);
//        recyclerView = findViewById(R.id.rv);//리사이클뷰
        // Toolbar toolbar = findViewById(R.id.toolbar);//툴바
        //setSupportActionBar(toolbar);//툴바지원
        //-> 플로팅 버튼 프래그 먼트로 옮김

        //-> fragment 로 옮김

 /*       //데이터삽입
        for (int i=0; i < 20; i++){
            list.add(new Note(R.mipmap.ic_launcher, "list " + i + "번째", "값 " + i));
        }*/

/*        // recyclerView에 LinearLayoutManager 객체 지정.
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        RecyclerAdapter adapter = new RecyclerAdapter(list) ;
        recyclerView.setAdapter(adapter) ;

        //클릭 이벤트
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        }) ;*/
    }
}

