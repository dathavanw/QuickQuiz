package com.example.quickquizapp;

public class HoatDongModel {
    private String thoiGian;
    private String maBai;
    private String diem;

    public HoatDongModel(String thoiGian, String maBai, String diem) {
        this.thoiGian = thoiGian;
        this.maBai = maBai;
        this.diem = diem;
    }
    public String getMaBai() { return maBai; }
    public String getDiem() { return diem; }
}

