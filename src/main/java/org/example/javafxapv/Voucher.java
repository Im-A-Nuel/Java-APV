package org.example.javafxapv;


import java.util.Date;

public class Voucher {


    int idVoucher;
    String namaVoucher;
    String jenis;
    String kategori;
    java.sql.Date tanggal;

    public Voucher(int idVoucher, String namaVoucher, String jenis, java.sql.Date tanggal, String kategori) {
        this.idVoucher = idVoucher;
        this.namaVoucher = namaVoucher;
        this.jenis = jenis;
        this.kategori = kategori;
        this.tanggal = tanggal;
    }

    public int getIdVoucher() {
        return idVoucher;
    }

    public void setIdVoucher(int idVoucher) {
        this.idVoucher = idVoucher;
    }

    public String getNamaVoucher() {
        return namaVoucher;
    }

    public void setNamaVoucher(String namaVoucher) {
        this.namaVoucher = namaVoucher;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(java.sql.Date tanggal) {
        this.tanggal = tanggal;
    }
}
