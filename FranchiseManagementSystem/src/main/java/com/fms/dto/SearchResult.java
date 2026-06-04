package com.fms.dto;

import java.io.Serializable;

public class SearchResult implements Serializable {

    private String type;
    private String name;
    private String extra;



    public SearchResult(
            String type,
            String name,
            String extra) {

        this.type = type;
        this.name = name;
        this.extra = extra;
       
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
    
}