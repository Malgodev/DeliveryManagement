package model;


import java.sql.Date;

public class Tracking {
    private Date date;
    private String address;

    public Tracking(Date date, String address){
        this.date = date;
        this.address = address;
    }

    public Date getDate(){
        return date;
    }

    public String getAddress(){
        return address;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public void setAddress(String address){
        this.address = address;
    }
}
