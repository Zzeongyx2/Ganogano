package kr.ac.jbnu.se.mobile.ganogano;


public class Practice{
    private String aperiod, bperiod;
    private String hospital;
    private String key;

    public Practice(){

    }

    public Practice(String aperiod, String bperiod, String hospital, String key){
        this.aperiod = aperiod;
        this.bperiod = bperiod;
        this.hospital = hospital;
        this.key = key;
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
}
