package com.example.appkabombi;

public class PictureInfo {

    String path;
    String label;
    int idInDB;

    public PictureInfo(String path, String label, int idInDB){
        this.path = path;
        this.label = label;
        this.idInDB = idInDB;
    }
}
