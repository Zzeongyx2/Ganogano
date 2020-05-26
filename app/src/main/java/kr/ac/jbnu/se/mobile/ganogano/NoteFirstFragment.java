package kr.ac.jbnu.se.mobile.ganogano;
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

import java.util.ArrayList;
import java.util.List;

// TODO : 실습노트 리스트 출력하는 액티비티

public class NoteFirstFragment extends Fragment {
    RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    List<Note> list = new ArrayList<>(); //혹시 모를 list

    @Override //onCreateView 전에 호출
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
    }

    @Override //onCreate 호출 후 호출
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.note_fragment_first, container, false);
        recyclerView = rootView.findViewById(R.id.rv);//리사이클뷰
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapter(getActivity(),list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return rootView;        // Inflate the layout for this fragment
    }

    //새로운 뷰로 넘어가기 ( 다음 프레그먼트를 의미) 여기서는 실습노트 -> 간호술기리스트로 넘어가기
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("hospital_name", String.valueOf(position));//TODO : position에 따른 값 가져오기

                NavHostFragment.findNavController(NoteFirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment,bundle);
            }
        });

        adapter.setOnItemLongClickListener(new RecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {
                //TODO : 선택지 나오게 하기
                Toast.makeText(getContext(),"롱클릭",Toast.LENGTH_SHORT).show();
            }
        });

/*        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(NoteFirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/
    }

    //임시데이터
    public void prepareData(){
        list.add(new Note("안녕","하세요","시험"));
        list.add(new Note("뱃보이","사이코","피카부"));
        list.add(new Note("안녕","하세요","시험"));
        list.add(new Note("안녕","하세요","시험"));

    }
}
