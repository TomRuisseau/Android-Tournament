package com.example.tournament.dataClasses;

public class Candidate {
    private String name;
    private String imageUrl;
    private String type;
    private int count;

    public Candidate(String name, String imageUrl, String type) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.type = type;
        this.count = 0;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
