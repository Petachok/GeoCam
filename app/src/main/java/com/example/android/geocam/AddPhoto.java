package com.example.android.geocam;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class AddPhoto extends AppCompatActivity{

    private ImageView mPhotoPreview;
    private EditText mPhotoDescription;
    private TextView mPhotoLocation;
    private Button mApproveB;
    private Button mCancelB;
    static final int REQUEST_IMAGE_CAPTURE = 1; //request code of image capture
    private LocationDetector myloc;
    double myLat = 0.0;
    double myLong = 0.0;

    private GeoPhoto geoPhoto;

    @Override
    @RequiresPermission (Manifest.permission.ACCESS_FINE_LOCATION)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        mPhotoPreview = (ImageView) findViewById(R.id.image_photo_pre);
        mPhotoDescription = (EditText) findViewById(R.id.text_photo_description);
        mPhotoLocation = (TextView) findViewById(R.id.text_photo_long_lang);
        mApproveB = (Button) findViewById(R.id.action_approve);
        mCancelB = (Button) findViewById(R.id.action_cancel);

        geoPhoto = new GeoPhoto();

        //LocationDetectorOBject for fetching the location where the photo is taken
        myloc = new LocationDetector(AddPhoto.this);

        //intent to take a picture using the camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

        // Removing Description text to allow input of The Users description of the photo
        mPhotoDescription.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mPhotoDescription.getText().toString().compareTo(getResources().getString(R.string.et_photo_description)) == 0)
                    mPhotoDescription.setText("");
            }
        });
    }

    @Override
    public void onBackPressed() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPhotoDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    geoPhoto.setPhotoDescription(mPhotoDescription.getText().toString());
                }
                return false;
            }
        });

        mApproveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng photoLatLng = new LatLng(myLat,myLong);
                geoPhoto.setPhotoLocation(photoLatLng);
            }
        });
        mCancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mPhotoPreview.setImageBitmap(imageBitmap);
        }
        if (myloc.canGetLocation) {
            myLat = myloc.getLatitude();
            myLong = myloc.getLongitude();
            String latLngToPrint = String.format("Lat: %.3f Long: %.3f",myLat,myLong); // message to be shown on the screen

            Log.v("Location values", "Latitude: " + myLat + " Longitude: " + myLong);
            mPhotoLocation.setText(latLngToPrint);
        }
    }

}

