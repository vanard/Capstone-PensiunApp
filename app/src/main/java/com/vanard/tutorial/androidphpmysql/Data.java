package com.vanard.tutorial.androidphpmysql;

public class Data {
    private String id, nama, unitkerja, alasan, tgl_pensiun, tgl_pengajuan,status, keterangan;

    public Data(){}
    public Data(String id, String nama, String alasan, String tgl_pensiun, String tgl_pengajuan, String status, String keterangan){
        this.id = id;
        this.nama = nama;
        this.alasan = alasan;
        this.tgl_pensiun = tgl_pensiun;
        this.tgl_pengajuan = tgl_pengajuan;
        this.status = status;
        this.keterangan = keterangan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUnitkerja() {
        return unitkerja;
    }

    public void setUnitkerja(String unitkerja) {
        this.unitkerja = unitkerja;
    }

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }

    public String getTgl_pensiun() {
        return tgl_pensiun;
    }

    public void setTgl_pensiun(String tgl_pensiun) {
        this.tgl_pensiun = tgl_pensiun;
    }

    public String getTgl_pengajuan() {
        return tgl_pengajuan;
    }

    public void setTgl_pengajuan(String tgl_pengajuan) {
        this.tgl_pengajuan = tgl_pengajuan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    @Override
    public String toString() {
        return keterangan;
    }
}
