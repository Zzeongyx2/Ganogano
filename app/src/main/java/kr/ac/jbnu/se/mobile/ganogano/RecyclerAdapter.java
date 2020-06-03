package kr.ac.jbnu.se.mobile.ganogano;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
/*
* RecycleView와 관련있는 클래스 목록
* 1. RecycleAdapter << 리사이클 뷰의 이벤트나 아이템 속에 들어갈 내용을 관리하는 클래스
* 2. content_format.xml << 리스트 내부의 (프로필, 윗줄텍스트, 아랫줄 택스트로 구성 된)양식을 정하는 xml
* 3. note_fragment_first.xml, note_second_first.xml 와 NoteFirstFragment, NoteSecondFragment
* 4. note_content.xml << 프래그먼트들을 담는 레이아웃 (프래그먼트들의 틀..?)
* 5. note_activity.xml << 모든 프래그먼트와 플로팅버튼을 투영하는 액티비티*/

//Recycle 뷰를 관리하는 어뎁터
//참고 : https://recipes4dev.tistory.com/154

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<Data> listData; //어뎁터가 가지고 있는 list

    RecyclerAdapter(ArrayList<Data> list) {//생성자2
        this.listData = list;
    }

    @NonNull
    @Override //아이템 뷰를 위한 뷰 홀더 객체를 생성하여 리턴
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_format, parent, false); //아이템을 담을 뷰
        return new ViewHolder(view);
    }

    @Override //뷰에 아이템 매핑(넣어주기) 및 아이템들을 보여주기
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {// RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(Data data) {//외부에서 추가시킬 함수입니다
        listData.add(data);
    }

    //내부 클래스로써. 하나의 View를 보존하는 역활을 하는 클래스 입니다.
    class ViewHolder extends RecyclerView.ViewHolder {
        //item의 구성과 같습니다 item의 구성은 content_format.xml 에서 확인 가능합니다.
        private TextView textView1, textView2;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            imageView = itemView.findViewById(R.id.imageView);

            //리스너가 클릭 이벤트 발생시 아래에 구현되었던 인터페이스 실행
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

                        if (mListener != null) {// 리스너 객체의 메서드 호출.
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });

            //롱 클릭 이벤트 발생 시 아래에 구현되었던 인터페이스 실행
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition(); //위치 받아오는 코드입니다.
                    if (pos != RecyclerView.NO_POSITION) {
                        if (lListener != null) {
                            lListener.onItemLongClick(v, pos);
                        }
                    }
                    return true;
                }
            });
        }
        //생성 된 아이템을 하나하나 매칭 시키는 메소드입니다.
        void onBind(Data data) {
            textView1.setText(data.getTitle());
            textView2.setText(data.getContent());
            imageView.setImageResource(data.getResId());
        }
    }

    //================클릭 이벤트 처리===========================//
    // 외부에서 이벤트를 처리할 수 있도록 리스너 인터페이스 구현
    //클릭 리스너 인터페이스 정의

    // 클릭, 롱클릭 리스너를 정의한 것
    private OnItemClickListener mListener = null;
    private OnItemLongClickListener lListener = null;

    //클릭 인터페이스 들
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int position);
    }

    //클릭되었을 때 OnITemClickListener 의 객체를 참조(생성)하는 메소드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
    //롱 클릭되었을 때 OnITemClickListener 의 객체를 참조(생성)하는 메소드
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.lListener = listener;
    }

}


