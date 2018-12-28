package com.example.rahaf.safeheart1;

public class Family {
    private String name;
    private String num;
    private String ID;
    private String patient_id;

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public Family(String ID, String name, String num , String patient_id) {
        this.name = name;
        this.num = num;
        this.ID = ID;
        this.patient_id = patient_id;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
