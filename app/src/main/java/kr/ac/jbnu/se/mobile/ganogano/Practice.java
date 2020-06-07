package kr.ac.jbnu.se.mobile.ganogano;


import java.util.ArrayList;

public class Practice {
    private String aperiod, bperiod;
    private String hospital;
    private String key;
    private ArrayList<Integer> day; // 년 월 일 을 저장하는 변수

    public Practice() {
        day = new ArrayList<>();
    }

    public Practice(String aperiod, String bperiod, String hospital, String key, ArrayList<Integer> day) {
        this.aperiod = aperiod;
        this.bperiod = bperiod;
        this.hospital = hospital;
        this.key = key;
        this.day = day;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getAperiod() {
        return aperiod;
    }

    public void setAperiod(String aperiod) {
        this.aperiod = aperiod;
    }

    public String getBperiod() {
        return bperiod;
    }

    public void setBperiod(String bperiod) {
        this.bperiod = bperiod;
    }

    public ArrayList<Integer> getDay() {
        return day;
    }

    public void setDay(ArrayList<Integer> day){
        this.day=day;
    }
}
