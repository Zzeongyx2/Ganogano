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
import java.util.Arrays;
import java.util.List;

// TODO : 간호술기 리스트 출력하는 액티비티

public class NoteSecondFragment extends Fragment {
    //TODO : 리사이클 뷰 구현하기
    RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    ArrayList<Data> list = new ArrayList<>(); //혹시 모를 list

    @Override //onCreateView 전에 호출
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
        String msg = getArguments().getString("hospital_name"); //번들내용 가져오기
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.note_fragment_second, container, false);
        recyclerView = rootView.findViewById(R.id.rv);//리사이클뷰
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapter(list); //리스트 넣기

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

    private void prepareData() {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList("국화", "사막", "수국", "해파리", "코알라", "등대", "펭귄", "튤립",
                "국화", "사막", "수국", "해파리", "코알라", "등대", "펭귄", "튤립");
        List<String> listContent = Arrays.asList(
                "이 꽃은 국화입니다.",
                "여기는 사막입니다.",
                "이 꽃은 수국입니다.",
                "이 동물은 해파리입니다.",
                "이 동물은 코알라입니다.",
                "이것은 등대입니다.",
                "이 동물은 펭귄입니다.",
                "이 꽃은 튤립입니다.",
                "이 꽃은 국화입니다.",
                "여기는 사막입니다.",
                "이 꽃은 수국입니다.",
                "이 동물은 해파리입니다.",
                "이 동물은 코알라입니다.",
                "이것은 등대입니다.",
                "이 동물은 펭귄입니다.",
                "이 꽃은 튤립입니다."
        );
        List<Integer> listResId = Arrays.asList(
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background
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
