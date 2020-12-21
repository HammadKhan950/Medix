package com.hammadkhan950.newotp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hammadkhan950.newotp.model.AdminData;
import com.hammadkhan950.newotp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder> {

    private ArrayList<AdminData> mAdminData;


    public AdminAdapter(ArrayList<AdminData> adminData) {
        mAdminData = adminData;

    }


    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_data, parent, false);
        AdminAdapter.AdminViewHolder adminViewHolder = new AdminAdapter.AdminViewHolder(view);
        return adminViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String todaysdate = dateFormat.format(date);
        AdminData currentItem = mAdminData.get(position);
        if (currentItem.getDate().equals(todaysdate)) {
            holder.llAdmin.setBackgroundColor(Color.CYAN);
        }
        holder.tvDate.setText(currentItem.getDate());
        holder.tvTime.setText(currentItem.getTime() + " pm");
        holder.tvCategory.setText(currentItem.getCategory());
        holder.tvName.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return mAdminData.size();
    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime, tvCategory, tvName;
        LinearLayout llAdmin;

        public AdminViewHolder(@NonNull View view) {
            super(view);
            tvCategory = view.findViewById(R.id.tv_category);
            tvDate = view.findViewById(R.id.tv_date);
            tvTime = view.findViewById(R.id.tv_time);
            tvName = view.findViewById(R.id.tv_name);
            llAdmin = view.findViewById(R.id.llAdmin);


        }
    }
}
