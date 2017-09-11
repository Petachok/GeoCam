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
}