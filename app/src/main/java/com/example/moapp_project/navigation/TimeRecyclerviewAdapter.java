package com.example.moapp_project.navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moapp_project.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TimeRecyclerviewAdapter extends RecyclerView.Adapter<TimeRecyclerviewAdapter.ViewHolder> {

    private List<String> timeList;

    public TimeRecyclerviewAdapter(List<String> timeList) {
        this.timeList = timeList;
        Collections.sort(timeList, new Comparator<String>() {
            @Override
            public int compare(String time1, String time2) {
                long timeValue1 = Long.parseLong(time1);
                long timeValue2 = Long.parseLong(time2);
                return -Long.compare(timeValue1, timeValue2); // 결과를 반대로 반환
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String time = timeList.get(position);
        Log.e("time", time);
        int seconds = Integer.parseInt(time);
        String formattedTime = formatTime(seconds);
        holder.bind(formattedTime);
        holder.bind2(position);
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView timeTextView;
        private TextView dayitemTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timetextview);
            dayitemTextView= itemView.findViewById(R.id.dayitemTextView);
        }

        public void bind(String time) {
            timeTextView.setText(time);
        }
        public void bind2(int position){
            position=position+1;
            String pos= Integer.toString(position);
            dayitemTextView.setText(pos);
        }
    }

    private String formatTime(int mille) {
        int seconds= mille/1000;
        int hour= seconds/3600;
        int minutes= (seconds-hour*3600)/60;
        seconds= seconds-(hour*3600)-(minutes*60);
        return String.format("%02d:%02d:%02d",hour, minutes, seconds);
    }
}
