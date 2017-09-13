package com.example.android.geocam;

import android.graphics.Path;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;

// Class for storing the data of a new added photo
public class GeoPhoto {
    private File takenPhotoPath;
    private String photoDescription;
    private LatLng photoLocation;

    public GeoPhoto(){
        photoDescription = new String();
        photoLocation = new LatLng(0,0);
        takenPhotoPath = new File("");
    }

    //PhotoDescription Getter
    public String getPhotoDescription(){
        return photoDescription;
    }
    //PhotoDescription Setter
    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }
    //PhotoLocation Getter
    public LatLng getPhotoLocation() {
        return photoLocation;
    }
    //PhotoLocation Setter
    public void setPhotoLocation(LatLng photoLocation) {
        this.photoLocation = photoLocation;
    }
    //Photo Saved Path Getter
    public File getTakenPhotoPath() {
        return takenPhotoPath;
    }
    //Photo Saved Path Setter
    public void setTakenPhotoPath(File takenPhotoPath) {
        this.takenPhotoPath = takenPhotoPath;
    }
}