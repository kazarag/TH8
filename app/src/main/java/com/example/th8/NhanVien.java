package com.example.th8;

import java.io.Serializable;

public class NhanVien extends PhongBan implements Serializable {
    int manv;
    String tennv;
    int maph;
    public NhanVien() {}

    public NhanVien(int manv, String tennv,int maph) {
        this.manv = manv;
        this.tennv = tennv;

    }

    public int getManv() {
        return manv;
    }

    public String getTennv() {
        return tennv;
    }

    public void setManv(int manv) {
        this.manv = manv;
    }

    public void setTennv(String tennv) {
        this.tennv = tennv;
    }

    @Override
    public String toString() {
        return new String(this.manv + "-" + this.tennv);
    }
}
