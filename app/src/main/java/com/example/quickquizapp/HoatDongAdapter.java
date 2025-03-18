package com.example.quickquizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HoatDongAdapter extends RecyclerView.Adapter<HoatDongAdapter.ViewHolder> {

    private List<HoatDongModel> hoatDongList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(HoatDongModel item);
    }

    public HoatDongAdapter(List<HoatDongModel> hoatDongList, OnItemClickListener listener) {
        this.hoatDongList = hoatDongList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hoat_dong, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HoatDongModel hoatDong = hoatDongList.get(position);
        //holder.txtThoiGian.setText(hoatDong.getThoiGian());
        holder.txtMaBai.setText("Mã bài: " + hoatDong.getMaBai());
        holder.txtDiem.setText("Điểm: " + hoatDong.getDiem());

        holder.btnXemChiTiet.setOnClickListener(v -> listener.onItemClick(hoatDong));
    }

    @Override
    public int getItemCount() {
        return hoatDongList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtThoiGian, txtMaBai, txtDiem;
        Button btnXemChiTiet;

        public ViewHolder(View itemView) {
            super(itemView);
            txtThoiGian = itemView.findViewById(R.id.txtThoiGian);
            txtMaBai = itemView.findViewById(R.id.txtMaBai);
            txtDiem = itemView.findViewById(R.id.txtDiem);
            btnXemChiTiet = itemView.findViewById(R.id.btnXemChiTiet);
        }
    }
}


