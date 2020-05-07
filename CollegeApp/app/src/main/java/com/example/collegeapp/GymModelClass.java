package com.example.collegeapp;

public class GymModelClass {

    private int image;
    private String TypeName;
    private String description;

    public GymModelClass(int image, String typeName, String description) {
        this.image = image;
        TypeName = typeName;
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public String getTypeName() {
        return TypeName;
    }

    public String getDescription() {
        return description;
    }
}
