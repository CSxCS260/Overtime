package com.example.overtime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ItemVH> {
    private static final String TAG = "Adapter";
    List<Alarm> alarmList;
    OnLongClickListener onLongClickListener;

    public interface OnLongClickListener{
        void onItemLongClicked(int position);
    }


    public Adapter(List<Alarm> alarmList, OnLongClickListener longClickListener) {
        this.alarmList = alarmList;
        onLongClickListener = longClickListener;
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
        holder.onOrOff.setChecked(alarm.isOnOrOff());
        holder.onOrOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm.setOnOrOff(!alarm.isOnOrOff());
            }
        });
        String daysofWeekString = "";
        for (int i = 0; i < alarm.getDaysOfWeek().size(); i++){
            daysofWeekString += alarm.getDaysOfWeek().get(i) +", ";
        }
        if(daysofWeekString.length()>0){
            daysofWeekString = daysofWeekString.substring(0, daysofWeekString.length()-2);
        }
        holder.collapsedLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickListener.onItemLongClicked(holder.getAdapterPosition());
                return false;
            }
        });
        holder.daysOfWeekTV.setText(daysofWeekString);
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
        SwitchCompat onOrOff;
        public ItemVH(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.title);
            timeTV = itemView.findViewById(R.id.time);
            amOrPmTV = itemView.findViewById(R.id.amOrPm);
            daysOfWeekTV = itemView.findViewById(R.id.daysOfTheWeek);
            onOrOff = itemView.findViewById(R.id.alarmSwitch);
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
