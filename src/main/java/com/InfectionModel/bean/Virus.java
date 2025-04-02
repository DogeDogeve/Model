package com.InfectionModel.bean;

import java.util.List;

public class Virus {
    public Virus(){}
    private String info;
    private int r0;
    private List<Double> symptomRate;
    private double deadRate;
    private int latentPeriod;
    private List<String> transmissionWay;


    public void setR0(int r0) {
        this.r0 = r0;
    }

    public List<Double> getSymptomRate() {
        return symptomRate;
    }

    public void setSymptomRate(List<Double> symptomRate) {
        this.symptomRate = symptomRate;
    }

    public double getDeadRate() {
        return deadRate;
    }

    public void setDeadRate(double deadRate) {
        this.deadRate = deadRate;
    }

    public int getLatentPeriod() {
        return latentPeriod;
    }

    public void setLatentPeriod(int latentPeriod) {
        this.latentPeriod = latentPeriod;
    }

    public List<String> getTransmissionWay() {
        return transmissionWay;
    }

    public void setTransmissionWay(List<String> transmissionWay) {
        this.transmissionWay = transmissionWay;
    }

    public int getR0() {
        return r0;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
