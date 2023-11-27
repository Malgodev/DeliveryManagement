package model;

public class Parcel {
    private int id;
    private double weight;
    private int transportId;
    private String title, note, sender, receiver;
    private Integer COD, COD_status, status;

    public Parcel(int id, double weight, int transportId, String title, String note, Integer COD, Integer COD_status, Integer status, String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
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

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getCOD_status() {
        if(COD == 0) return "No COD";
        if (COD_status == 0) return "Haven't paid";
        else return "Paid";
    }

    public String getStatus() {
        return switch (status) {
            case 0 -> "Wait for transiting";
            case 1 -> "Transiting";
            case 2 -> "Delivering";
            case 3 -> "Wait for re-delivering";
            case 4 -> "Delivered";
            case 5 -> "Processing";
            case 6 -> "Wait for returning";
            case 7 -> "Returning";
            case 8 -> "Returned";
            default -> "404 not found";
        };
    }

    public void setTitle(String text) {
        this.title = text;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
