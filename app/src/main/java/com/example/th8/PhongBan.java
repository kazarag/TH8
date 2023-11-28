package com.example.th8;

public class PhongBan {
    int maph;
    public String tenph;
    String mota;

    public PhongBan() {}

    public PhongBan(int maph, String tenph) {
        this.maph = maph;
        this.tenph = tenph;
    }

    public int getMaph() {
        return maph;
    }

    public String getTenph() {
        return tenph;
    }
    public String getMota() {
        return mota;
    }

    public void setMaph(int maph) {
        this.maph = maph;
    }

    public void setTenph(String tenph) {
        this.tenph = tenph;
    }
    public void setMota(String mota) {
        this.mota = mota;
    }
    @Override
    public String toString() {
        return new String(this.maph + "-" + this.tenph);
    }
}
