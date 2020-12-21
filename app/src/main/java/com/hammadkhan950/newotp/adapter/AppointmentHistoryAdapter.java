package com.hammadkhan950.newotp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hammadkhan950.newotp.R;
import com.hammadkhan950.newotp.model.Appointment;

import java.util.ArrayList;

public class AppointmentHistoryAdapter extends RecyclerView.Adapter<AppointmentHistoryAdapter.AppoinmentViewHolder> {
    private ArrayList<Appointment> mAppointments;


    public AppointmentHistoryAdapter(ArrayList<Appointment> appointments) {
        mAppointments = appointments;

    }

    @NonNull
    @Override
    public AppoinmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appoinments_history, parent, false);
        AppoinmentViewHolder appoinmentViewHolder = new AppoinmentViewHolder(view);
        return appoinmentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppoinmentViewHolder holder, int position) {


        Appointment currentItem = mAppointments.get(position);
        holder.tvTime.setText(currentItem.getTime()+" pm");
        holder.tvDate.setText(currentItem.getDate());
        holder.tvCategory.setText(currentItem.getCategory());

    }

    @Override
    public int getItemCount() {
        return mAppointments.size();
    }

    public static class AppoinmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime, tvCategory;

        public AppoinmentViewHolder(@NonNull View view) {
            super(view);
            tvCategory = view.findViewById(R.id.tv_category);
            tvDate = view.findViewById(R.id.tv_date);
            tvTime = view.findViewById(R.id.tv_time);


        }
    }
}
