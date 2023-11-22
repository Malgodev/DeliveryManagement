package model;

public class Parcel {
    private int id;
    private double weight;
    private int transportId;
    private String title, note;
    private int COD, COD_status, status;

    public Parcel(int id, double weight, int transportId, String title, String note, int COD, int COD_status, int status) {
        this.id = id;
        this.weight = weight;
        this.transportId = transportId;
        this.title = title;
        this.note = note;
        this.COD = COD;
        this.COD_status = COD_status;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public double getWeight() {
        return weight;
    }

    public int getTransportId() {
        return transportId;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public int getCOD() {
        return COD;
    }

    public String getCOD_status() {
        if (COD_status == 0) return "Haven't paid";
        else return "Paid";
    }

    public String getStatus() {
        if (status == 0) return "Sending";
        else return "Sent";
    }
}
