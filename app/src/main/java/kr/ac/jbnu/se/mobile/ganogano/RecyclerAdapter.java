package kr.ac.jbnu.se.mobile.ganogano;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private List<Note> list; //리스트 Note 클래스는 Note에 저장되어야 하는 거
    private Context context; // 프래그먼트를 위한 context

    RecyclerAdapter(List<Note> list){//생성자
        this.list = list;
    }

    RecyclerAdapter(Context context, List<Note> list){//생성자2
        this.list = list;
        this.context=context;
    }
    @NonNull
    @Override //아이템 뷰를 위한 뷰 홀더 객체를 생성하여 리턴
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_format, parent, false); //아이템을 담을 뷰
        return new ViewHolder(view);
    }

    @Override //뷰에 아이템 매핑 아이템뷰 보여주기
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView1.setText(list.get(position).tv1); //위치에 따라 리스트 출력
        holder.textView2.setText(list.get(position).tv2);
        //아래는 클릭 리스너 인터페이스로 구현해서 옮겨놓음
//        holder.textView1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), list.get(position).tv1, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        holder.textView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), list.get(position).tv2, Toast.LENGTH_SHORT).show();
//            }
//        });
        //text3는 필요할 때 넣기 이미지 뷰 필요한 경우 나중에
    }
    //하나의 View를 보존하는 역활을 한다.
    class ViewHolder extends RecyclerView.ViewHolder { //뷰 홀더에 아이템들 담기, 문제시 외부 클래스로 뺴기
        TextView textView1, textView2, textView3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);

            //리스너가 클릭 이벤트 일 때 아래 함수 실행
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos) ;
                        }
                    }
                }
            });
            
            //리스너가 롱 클릭 일 때 아래 함수 실행
            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(lListener != null){
                            lListener.onItemLongClick(v,pos);
                        }
                    }
                    return true;
                }
            });
        }
    }
    @Override //크기반환
    public int getItemCount() {
        return list.size();
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null ;
    private OnItemLongClickListener lListener = null ;

// 외부에서 이벤트를 처리할 수 있도록 리스너 인터페이스 구현
    //클릭 리스너 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int position) ;
    }

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }
    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.lListener = listener ;
    }
    /*==================================================*/
}


