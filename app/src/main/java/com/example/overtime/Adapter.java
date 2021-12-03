package com.example.overtime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ItemVH> {
    private static final String TAG = "Adapter";
    List<Alarm> alarmList;

    public Adapter(List<Alarm> alarmList) {
        this.alarmList = alarmList;
    }


    @NonNull
    @Override
    public Adapter.ItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expanded_item, parent, false);
        return new ItemVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ItemVH holder, int position) {
        Alarm alarm = alarmList.get(position);
        holder.titleTV.setText(alarm.getTitle());
        String alarmString;
        Date alarmDate = alarm.getTime();
        if (alarmDate.getMinutes() < 10){
            alarmString = alarmDate.getHours()%12 + ":0" + alarmDate.getMinutes();
        }
        else{
            alarmString = alarmDate.getHours()%12 + ":" + alarmDate.getMinutes();
        }
        holder.timeTV.setText(alarmString);
        holder.amOrPmTV.setText(alarm.getAmOrPm());
        holder.daysOfWeekTV.setText(alarm.getDaysOfWeek());
        boolean isExpanded = alarmList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE:View.GONE);
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public class ItemVH extends RecyclerView.ViewHolder{
        private static final String TAG = "Item";
        TextView titleTV, timeTV, amOrPmTV, daysOfWeekTV;
        ConstraintLayout collapsedLayout, expandableLayout;
        public ItemVH(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.title);
            timeTV = itemView.findViewById(R.id.time);
            amOrPmTV = itemView.findViewById(R.id.amOrPm);
            daysOfWeekTV = itemView.findViewById(R.id.daysOfTheWeek);
            expandableLayout = itemView.findViewById(R.id.expandedLayout);
            collapsedLayout = itemView.findViewById(R.id.collapsedLayout);
            collapsedLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Alarm alarm = alarmList.get(getAdapterPosition());
                    alarm.setExpanded(!alarm.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }
}
