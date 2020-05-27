package kr.ac.jbnu.se.mobile.ganogano;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

// TODO : 실습노트 리스트 출력하는 액티비티

public class NoteFirstFragment extends Fragment {
    RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    ArrayList<Data> list = new ArrayList<>(); //혹시 모를 list

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
        adapter = new RecyclerAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return rootView;        // Inflate the layout for this fragment
    }

    //새로운 뷰로 넘어가기 ( 다음 프레그먼트를 의미) 여기서는 실습노트 -> 간호술기리스트로 넘어가기
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //그냥 클릭시 다음 프레그먼트 (간호수기) 실행
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("hospital_name", String.valueOf(position));//TODO : position에 따른 값 가져오기

                NavHostFragment.findNavController(NoteFirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment,bundle);
            }
        });
        //롱클릭
        adapter.setOnItemLongClickListener(new RecyclerAdapter.OnItemLongClickListener() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            @Override
            public void onItemLongClick(View v, int position) {
                builder.setItems(R.array.LAN, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO : 선택지에 따른 수정과 삭제페이지 이동하기
                        String[] items = getResources().getStringArray(R.array.LAN);//해당 항목리스트 받아와서 출력
                        Toast.makeText(getContext(),items[which],Toast.LENGTH_LONG).show(); //지금은 클릭한거 보여주는 거
                    }
                });

                //Toast.makeText(getContext(),"롱클릭",Toast.LENGTH_SHORT).show();
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
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground
        );
        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setResId(listResId.get(i));
            list.add(data);
//            // 각 값이 들어간 data를 adapter에 추가합니다.
//            adapter.addItem(data);
        }

//        // adapter의 값이 변경되었다는 것을 알려줍니다.
//        adapter.notifyDataSetChanged();
    }

}
