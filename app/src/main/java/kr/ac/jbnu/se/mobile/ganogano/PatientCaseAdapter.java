package kr.ac.jbnu.se.mobile.ganogano;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PatientCaseAdapter extends BaseAdapter {
    private final List<PatientCase> mData;


    public PatientCaseAdapter(List<PatientCase> patientCaseList) {
        mData = patientCaseList;
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
            view = inflater.inflate(R.layout.item_case, null);

            PatientCaseAdapter.ViewHolder holder = new PatientCaseAdapter.ViewHolder();
            holder.sicknessTextView = (TextView)view.findViewById(R.id.sickness_text);
            holder.prescriptionTextView = (TextView)view.findViewById(R.id.prescription_text);
            holder.precautionTextView = (TextView)view.findViewById(R.id.precaution_text);
            holder.etcTextView = (TextView)view.findViewById(R.id.etc_text);

            view.setTag(holder);
        }

        PatientCase inputPatientCase = mData.get(position);

        if (inputPatientCase != null) {
            PatientCaseAdapter.ViewHolder holder = (PatientCaseAdapter.ViewHolder)view.getTag();
            holder.sicknessTextView.setText(inputPatientCase.getSickness());
            holder.prescriptionTextView.setText(inputPatientCase.getPrescription());
            holder.precautionTextView.setText(inputPatientCase.getPrecaution());
            holder.etcTextView.setText(inputPatientCase.getEtc());
        }
        return view;
    }

    private static class ViewHolder {
        TextView sicknessTextView;
        TextView prescriptionTextView;
        TextView precautionTextView;
        TextView etcTextView;
    }
}
