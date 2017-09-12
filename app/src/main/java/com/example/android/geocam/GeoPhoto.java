package com.example.android.geocam;

import android.graphics.Path;

import com.google.android.gms.maps.model.LatLng;

// Class for storing the data of a new added photo
public class GeoPhoto {
    private Path takenPhotoPath;
    private String photoDescription;
    private LatLng photoLocation;

    public GeoPhoto(){
        photoDescription = new String();
        photoLocation = new LatLng(0,0);
        takenPhotoPath = new Path();
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
    public Path getTakenPhotoPath() {
        return takenPhotoPath;
    }
    //Photo Saved Path Setter
    public void setTakenPhotoPath(Path takenPhotoPath) {
        this.takenPhotoPath = takenPhotoPath;
    }
}