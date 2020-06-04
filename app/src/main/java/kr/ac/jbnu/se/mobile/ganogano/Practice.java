package kr.ac.jbnu.se.mobile.ganogano;

import androidx.annotation.NonNull;

public class Practice{
    private String period;
    private String hospital;
    private String key;

    public Practice(){

    }

    public Practice(String period, String hospital, String key){
        this.period = period;
        this.hospital = hospital;
        this.key = key;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

}
