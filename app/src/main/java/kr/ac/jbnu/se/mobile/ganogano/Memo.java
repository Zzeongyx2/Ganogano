package kr.ac.jbnu.se.mobile.ganogano;

import androidx.annotation.NonNull;

public class Memo {
    private String title;
    private String content;
    private String key;

    public Memo(){

    }

    public Memo(String title, String content, String key){
        this.content = content;
        this.title = title;
        this.key = key;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
