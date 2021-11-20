package com.generatepdf.com;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class StudentAdapter extends ArrayAdapter<StudentModel> {private int sample=0;
    private int i;

    private Context context;
    public StudentAdapter(Context context, List<StudentModel> list, int i) {
        super(context, 0, list);
        this.context=context;
        this.i=i;
    }





    @SuppressLint("CheckResult")
    @Override
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final StudentModel studentModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }
        final TextView rollNumber =  convertView.findViewById(R.id.rollNumber);
        final TextView name =  convertView.findViewById(R.id.name);
        final TextView sClass =  convertView.findViewById(R.id.sClass);
        final TextView section =  convertView.findViewById(R.id.section);
        final TextView gender =  convertView.findViewById(R.id.gender);
        final TextView totalMarks =  convertView.findViewById(R.id.totalMarks);
        final LinearLayout linearLayoutList=convertView.findViewById(R.id.linearLayoutList);

        if(studentModel!=null && studentModel.getRollNumber()!=null) {


            rollNumber.setText(studentModel.getRollNumber());
            name.setText(studentModel.getName());
            sClass.setText(studentModel.getClasses());
            section.setText(studentModel.getSection());
            gender.setText(studentModel.getGender());
            totalMarks.setText(studentModel.getTotalMarks());
            linearLayoutList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        return convertView;
    }

}