package com.vientamthuong.eatsimple.admin.model;

import android.graphics.Bitmap;

import com.vientamthuong.eatsimple.date.DateTime;

public class DanhMuc {

    private String maDanhMuc;
    private String tenDanhMuc;
    private DateTime ngayTao;
    private int soSanPham;
    private boolean chonXoa;
    private String url;
    private String hinh_fb;
    private Bitmap hinh;

    public DanhMuc(String maDanhMuc, String tenDanhMuc, DateTime ngayTao, int soSanPham, boolean chonXoa, String url, Bitmap hinh) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
        this.ngayTao = ngayTao;
        this.soSanPham = soSanPham;
        this.chonXoa = chonXoa;
        this.url = url;
        this.hinh = hinh;
    }

    public DanhMuc(DanhMuc d){
        this.maDanhMuc = d.getMaDanhMuc();
        this.tenDanhMuc = d.getTenDanhMuc();
        this.ngayTao = d.getNgayTao();
        this.soSanPham = d.getSoSanPham();
        this.chonXoa = d.isChonXoa();
        this.url = d.getUrl();
        this.hinh = d.getHinh();
    }

    public String getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public DateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(DateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public int getSoSanPham() {
        return soSanPham;
    }

    public void setSoSanPham(int soSanPham) {
        this.soSanPham = soSanPham;
    }

    public boolean isChonXoa() {
        return chonXoa;
    }

    public void setChonXoa(boolean chonXoa) {
        this.chonXoa = chonXoa;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getHinh() {
        return hinh;
    }

    public void setHinh(Bitmap hinh) {
        this.hinh = hinh;
    }

    public String getHinh_fb() {
        return hinh_fb;
    }

    public void setHinh_fb(String hinh_fb) {
        this.hinh_fb = hinh_fb;
    }
}
