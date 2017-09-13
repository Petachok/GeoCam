package com.example.android.geocam;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.FileProvider;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPhoto extends AppCompatActivity implements View.OnClickListener{

    static final int REQUEST_IMAGE_CAPTURE = 1; //request code of image capture
    static final String TAG = "ADD_FILE";

    private ImageView mPhotoPreview;
    private EditText mPhotoDescription;
    private TextView mPhotoLocation;
    private Button mApproveB;
    private Button mCancelB;
    private LocationDetector myloc;
    double myLat = 0.0;
    double myLong = 0.0;

    String mCurrentPhotoPath;
    private GeoPhoto geoPhoto;
    private File photoFile;

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

        //click listeners for 'cancel' and 'approve' buttons
        mApproveB.setOnClickListener(this);
        mCancelB.setOnClickListener(this);

        geoPhoto = new GeoPhoto();

        //LocationDetectorOBject for fetching the location where the photo is taken
        myloc = new LocationDetector(AddPhoto.this);

        //intent to take a picture using the camera
        dispatchTakePictureIntent();

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_approve:   /* section handles the click on 'approve' button, user returned to the main screen with an object holding the image and the location */
                LatLng photoLatLng = new LatLng(myLat,myLong);
                geoPhoto.setPhotoLocation(photoLatLng);
                try {
                    geoPhoto.setTakenPhotoPath(photoFile);
                    Log.v("IMAGE_ADDED","Image added to the geoPhoto object, it's path: " + photoFile.getAbsolutePath());
                }catch(NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case R.id.action_cancel:       /* section handles the click on 'cancel' button, user returned to the main screen without saving anything */
                Intent i = new Intent();
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPhotoDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override   /* handling the removal of the hint for the photo description input */
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    geoPhoto.setPhotoDescription(mPhotoDescription.getText().toString());
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(photoFile.getPath());
            mPhotoPreview.setImageBitmap(imageBitmap);
        }
        if (myloc.canGetLocation) {
            myLat = myloc.getLatitude();
            myLong = myloc.getLongitude();
            String latLngToPrint = String.format("Lat: %.3f Long: %.3f",myLat,myLong); // message to be shown in the photo location textView

            Log.v("Location values", "Latitude: " + myLat + " Longitude: " + myLong);
            mPhotoLocation.setText(latLngToPrint);
        }
    }

    private File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";

        Log.v(TAG,"The name of the file is: " + imageFileName);

    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    File image = File.createTempFile(imageFileName,".jpg",storageDir);

    // Save a file: path for use with ACTION_VIEW intents
    mCurrentPhotoPath = image.getAbsolutePath();
        Log.v(TAG,"The path of the file is: " + mCurrentPhotoPath);
    return image;
    }

    private void dispatchTakePictureIntent() {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    // Ensure that there's a camera activity to handle the intent
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        // Create the File where the photo should go
        photoFile = null;
        try {
             photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            ex.printStackTrace();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.geocam.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }}

}

