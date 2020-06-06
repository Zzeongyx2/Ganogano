package kr.ac.jbnu.se.mobile.ganogano;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PracticeAdapter extends BaseAdapter {
    private List<Practice> mData;

    public PracticeAdapter(List<Practice> practiceList) {
        mData = practiceList;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
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
            view = inflater.inflate(R.layout.item_practice, null);

            PracticeAdapter.ViewHolder holder = new PracticeAdapter.ViewHolder();
            holder.periodTextView = (TextView)view.findViewById(R.id.period_text);
            holder.hospitalTextView = (TextView)view.findViewById(R.id.hospital_text);

            view.setTag(holder);
        }

        Practice inputPractice = mData.get(position);
        if (inputPractice != null) {
            PracticeAdapter.ViewHolder holder = (PracticeAdapter.ViewHolder)view.getTag();
            holder.periodTextView.setText(inputPractice.getPeriod());
            holder.hospitalTextView.setText(inputPractice.getHospital());
        }
        return view;
    }

    private static class ViewHolder {
        TextView periodTextView;
        TextView hospitalTextView;
    }
}
