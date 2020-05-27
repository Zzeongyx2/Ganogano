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

// TODO : 간호술기 리스트 출력하는 액티비티

public class NoteSecondFragment extends Fragment {
    //TODO : 리사이클 뷰 구현하기
    RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    List<Note> list = new ArrayList<>(); //혹시 모를 list

    @Override //onCreateView 전에 호출
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
        String msg = getArguments().getString("hospital_name"); //번들내용 가져오기
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.note_fragment_first, container, false);
        recyclerView = rootView.findViewById(R.id.rv);//리사이클뷰
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapter(getActivity(),list); //리스트 넣기

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

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
                        .navigate(R.id.action_SecondFragment_to_ThirdFragment,bundle);
            }
        });

        adapter.setOnItemLongClickListener(new RecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {
                //TODO : 선택지 나오게 하기
                Toast.makeText(getContext(),"롱클릭",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //임시데이터
    public void prepareData(){
        list.add(new Note("안녕","하세요","시험"));
        list.add(new Note("뱃보이","사이코","피카부"));
        list.add(new Note("안녕","하세요","시험"));
        list.add(new Note("안녕","하세요","시험"));
    }
}
