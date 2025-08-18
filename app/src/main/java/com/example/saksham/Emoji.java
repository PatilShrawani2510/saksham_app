package com.example.saksham;

public class Emoji {
    private int imageResourceId;
    private String name;

    public Emoji(int imageResourceId,String name){
        this.imageResourceId = imageResourceId;
        this.name = name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getName() {
        return name;
    }
}
