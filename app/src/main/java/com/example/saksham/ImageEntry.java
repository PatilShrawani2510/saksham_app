package com.example.saksham;

public class ImageEntry {
    private String imageUri;
    private String word;

    public ImageEntry(String imageUri, String word) {
        this.imageUri = imageUri;
        this.word = word;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word){
        this.word = word;
    }

    // You can add setters if needed, but for this use case, they are not necessary.
}