package kr.ac.jbnu.se.mobile.ganogano;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<Data> listData;

    RecyclerAdapter(ArrayList<Data> list) {//생성자2
        this.listData = list;
    }

    @NonNull
    @Override //아이템 뷰를 위한 뷰 홀더 객체를 생성하여 리턴
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_format, parent, false); //아이템을 담을 뷰
        return new ViewHolder(view);
    }

    @Override //뷰에 아이템 매핑 아이템뷰 보여주기
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {// RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(Data data) {//외부에서 추가시킬 함수
        listData.add(data);
    }

    //하나의 View를 보존하는 역활을 한다.
    class ViewHolder extends RecyclerView.ViewHolder { //뷰 홀더에 아이템들 담기, 문제시 외부 클래스로 뺴기
        private TextView textView1, textView2;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            imageView = itemView.findViewById(R.id.imageView);

            //리스너가 클릭 이벤트 일 때 아래 함수 실행
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });

            //리스너가 롱 클릭 일 때 아래 함수 실행
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (lListener != null) {
                            lListener.onItemLongClick(v, pos);
                        }
                    }
                    return true;
                }
            });
        }

        void onBind(Data data) {
            textView1.setText(data.getTitle());
            textView2.setText(data.getContent());
            imageView.setImageResource(data.getResId());
        }
    }

    //================클릭 이벤트 처리===========================//
    // 외부에서 이벤트를 처리할 수 있도록 리스너 인터페이스 구현
    //클릭 리스너 인터페이스 정의

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;
    private OnItemLongClickListener lListener = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int position);
    }

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.lListener = listener;
    }

}


