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

// TODO : 실습노트 리스트 출력하는 액티비티

/*1. 첫번째 프레그 먼트 프레그먼트의 이동경로는 navigation 파일을 확인할 것
* 2. 리사이클 뷰를 사용
* 3. 플로팅 버튼도 사용
* 4. 리사이클 뷰의 컨트롤과 액션은 "RecyclerAdapter"을 참고. RecyclerAdapter에 액션 인터페이스를 정의해놓음
* adapter.addItem(data);를 통해 추가
* adapter.notifyDataSetChanged();로 갑 변경을 알립니다.
* 아직 값이 유지되는지는 확인 불가*/

public class NoteFirstFragment extends Fragment {
    RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    ArrayList<Data> list = new ArrayList<>(); //혹시 모를 list
    FloatingActionButton fab; //플로팅버튼

    @Override //onCreateView 전에 호출되는 기본 메소드
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData(); //임시
    }

    @Override //onCreate 호출 후 호출되는 메소드 액티비티의 onCreate메소드와 같은 아이
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.note_fragment_first, container, false);
        recyclerView = rootView.findViewById(R.id.rv);//rv는 note_fragment_first에 삽입되어 있는 RecycleView 테그

        // ===========리사이클 뷰 기본 설정============== //
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        //=============================================//
        fab = rootView.findViewById(R.id.fab);//플로팅 버튼

        return rootView;        // Inflate the layout for this fragment
    }

    //새로운 뷰로 넘어가기 ( 다음 프레그먼트를 의미) 여기서는 실습노트 -> 간호술기리스트로 넘어가기
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //리스트들 중의 아이템을 클릭시 실행.
        // adapter가 이벤트를 받는 이유는 adapter에 액션을 구현해 놓았기 때문입니다.
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) { //아이템을 클릭시
                //일단은 번들안에 값을 넣어 놓았습니다 hospital_name으로 데이터베이스를 필터링해 간호술기 리스트에 보여줄 내용을 뿌려줍니다.
                Bundle bundle = new Bundle();
                bundle.putString("hospital_name", String.valueOf(position));//TODO : position에 따른 값 가져오기

                // 네비게이션을 참고! 두번째 프레그먼트로 넘어가는 코드입니다
                NavHostFragment.findNavController(NoteFirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
            }
        });

        //롱 클릭시
        adapter.setOnItemLongClickListener(new RecyclerAdapter.OnItemLongClickListener() {

            @Override
            public void onItemLongClick(View v, int position) {
                //위에 뜨는 창 구현 코드
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("원하는 작업을 선택해 주세요"); //창의 제목입니다

                //창 클릭시 이벤트
                builder.setItems(R.array.LAN, new DialogInterface.OnClickListener() { //LAN은 string.xml에 있으며 목록 xml 파일 입니다.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                       String[] items = getResources().getStringArray(R.array.LAN); //목록을 가져오는 코드
                        if (which == 0) {//첫번째를 클릭했다면
                            Intent I = new Intent(getActivity(), NoteRevisionActivity.class); //노트 수정 페이지로 이동. 노트 수정페이지는 Activity로 구현
                            startActivity(I);
                        } else {
                            //TODO : 삭제로직
                            //두번째를 클릭했다면 삭제
                            adapter.notifyDataSetChanged();//변화 적용하는 adapter
                        }
                    }
                });
                builder.show();
            }
        });

        //플로팅 버튼을 눌렀을 때 이벤트 처리 코드
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : +버튼 눌렀을 때 추가페이지 들어가기
                Intent I = new Intent(getActivity(), AddNoteActivity.class); //노트를 추가하는 페이지로 넘어가는 코드
                startActivity(I);
                //스넥바 빼도됨
//                Snackbar.make(view, "들어가는 중", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }


    private void prepareData() {
        // 초기 데이터 값, 추후 삭제할 것
        List<String> listTitle = Arrays.asList("R hospital", "E hospital", "D hospital", "V hospital", "E hospital", "L hospital", "V hospital", "E hospital",
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

        }


    }

}
