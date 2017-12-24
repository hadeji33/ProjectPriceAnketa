package com.androidmads.projectpriceanket;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by lairg on 18.12.2017.
 */

public class modelClass {
    private long id;
    private String name;
    private String date;
    private int posted;
    private String text;
    private String result;

    public Map<String,String> convertTextToMap (String text){
        Map<String,String> data = new HashMap<String,String>();
        String str = text.replace("{", "").replace("}", "");
        String[] arr = str.split(", ");
        for (String mapline : arr) {
            String[] splited = mapline.split("=");
            data.put(splited[0], splited[1]);
        }
        return data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPosted() {
        return posted;
    }

    public void setPosted(int posted) {
        this.posted = posted;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
