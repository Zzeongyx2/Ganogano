package kr.ac.jbnu.se.mobile.ganogano;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class MemoAdapter extends BaseAdapter {
    private final List<Memo> mData;


    public MemoAdapter(List<Memo> memoList) {
        mData = memoList;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.item_memo, null);

            ViewHolder holder = new ViewHolder();
            holder.titleTextView = (TextView) view.findViewById(R.id.title_text);
            holder.contentTextView = (TextView) view.findViewById(R.id.content_text);
            view.setTag(holder);
        }


        Memo inputMemo = mData.get(position);
        if (inputMemo != null) {
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.titleTextView.setText(inputMemo.getTitle());
            holder.contentTextView.setText(inputMemo.getContent());
        }
        return view;
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
    }

}