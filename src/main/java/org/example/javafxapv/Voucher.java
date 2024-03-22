package org.example.javafxapv;


import java.util.Date;

public class Voucher {

    private int idVoucher;
    private String user;
    private String namaVoucher;
    private String jenis;
    private String kategori;
    private java.sql.Date tanggal;
    private String instruksi = "-";
    private String batasan = "-";

    public Voucher(int idVoucher, String user ,String namaVoucher, String jenis, java.sql.Date tanggal, String kategori) {
        this.idVoucher = idVoucher;
        this.user = user;
        this.namaVoucher = namaVoucher;
        this.jenis = jenis;
        this.kategori = kategori;
        this.tanggal = tanggal;
    }


    public Voucher(int idVoucher, String user , String namaVoucher, String jenis, java.sql.Date tanggal, String kategori, String instruksi, String batasan) {
        this.idVoucher = idVoucher;
        this.user = user;
        this.namaVoucher = namaVoucher;
        this.jenis = jenis;
        this.kategori = kategori;
        this.tanggal = tanggal;
        this.instruksi = instruksi;
        this.batasan = batasan;
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

    public String getInstruksi() {
        return instruksi;
    }

    public void setInstruksi(String instruksi) {
        this.instruksi = instruksi;
    }

    public String getBatasan() {
        return batasan;
    }

    public void setBatasan(String batasan) {
        this.batasan = batasan;
    }
}
