import android.graphics.Path;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by natan on 11-Sep-17.
 */

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