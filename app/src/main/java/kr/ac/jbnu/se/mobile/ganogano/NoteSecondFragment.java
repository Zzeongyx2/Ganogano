package kr.ac.jbnu.se.mobile.ganogano;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// TODO : 간호술기 리스트 출력하는 액티비티

public class NoteSecondFragment extends Fragment {
    //TODO : 리사이클 뷰 구현하기
    RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    ArrayList<Data> list = new ArrayList<>(); //혹시 모를 list
    FloatingActionButton fab; //플로팅버튼

    @Override //onCreateView 전에 호출
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
        String msg = getArguments().getString("hospital_name"); //번들내용 가져오기
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.note_fragment_second, container, false);
        recyclerView = rootView.findViewById(R.id.rv);//리사이클뷰
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapter(list); //값 넣기
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        fab = rootView.findViewById(R.id.fab);

        return rootView;        // Inflate the layout for this fragment
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //TODO : position 에 따른 값 가져오기
                Bundle bundle = new Bundle();
                bundle.putString("hospital_name", String.valueOf(position));
                //=========================================================//

                NavHostFragment.findNavController(NoteSecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_ThirdFragment, bundle);
            }
        });

        adapter.setOnItemLongClickListener(new RecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {
                //TODO : 선택지 나오게 하기
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("원하는 작업을 선택해 주세요");
                builder.setItems(R.array.LAN, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent I = new Intent(getActivity(), NoteNurseRevisionActivity.class);
                            startActivity(I);
                        } else {
                            //TODO : 삭제로직
                            adapter.notifyDataSetChanged(); // 다시 불러오기
                        }
                    }
                });
                builder.show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //추가페이지 이동
                Intent I = new Intent(getActivity(), AddNurseNoteActivity.class);
                startActivity(I);
            }
        });
    }

    private void prepareData() {
        // 초기 데이터 값
        List<String> listTitle = Arrays.asList("A .", "E hospital", "D hospital", "V hospital", "E hospital", "L hospital", "V hospital", "E hospital",
                "T hospital");
        List<String> listContent = Arrays.asList(
                "200.01.01~200.01.01",
                "200.01.01~200.01.01",
                "200.01.01~200.01.01",
                "200.01.01~200.01.01",
                "200.01.01~200.01.01",
                "200.01.01~200.01.01",
                "200.01.01~200.01.01",
                "200.01.01~200.01.01",
                "200.01.01~200.01.01"
        );
        List<Integer> listResId = Arrays.asList( //이미지인데 굳이 안넣어도 됨
                R.drawable.ic_check_box_black_24dp,
                R.drawable.ic_check_box_black_24dp,
                R.drawable.ic_check_box_black_24dp,
                R.drawable.ic_check_box_black_24dp,
                R.drawable.ic_check_box_black_24dp,
                R.drawable.ic_check_box_black_24dp,
                R.drawable.ic_check_box_black_24dp,
                R.drawable.ic_check_box_black_24dp,
                R.drawable.ic_check_box_black_24dp
        );

        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setResId(listResId.get(i));
            list.add(data);
            //adapter.addItem(data); 값 추가
        }
        //adapter.notifyDataSetChanged();// adapter의 값이 변경되었다는 것을 알려줍니다.
    }
}
