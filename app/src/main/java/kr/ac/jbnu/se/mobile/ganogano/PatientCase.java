package kr.ac.jbnu.se.mobile.ganogano;
import androidx.annotation.NonNull;

public class PatientCase{
    private String sickness;
    private String prescription;
    private String precaution;
    private String etc;
    private String key;


    public PatientCase(String sickness, String prescription, String precaution, String etc, String key){
        this.sickness = sickness;
        this.prescription = prescription;
        this.precaution = precaution;
        this.etc = etc;
        this.key = key;
    }



    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSickness() {
        return sickness;
    }

    public void setSickness(String sickness) {
        this.sickness = sickness;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getPrecaution() {
        return precaution;
    }

    public void setPrecaution(String precaution) {
        this.precaution = precaution;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

}
