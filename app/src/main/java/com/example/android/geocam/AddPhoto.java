package com.example.android.geocam;

import android.Manifest;
import android.app.LauncherActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AddPhoto extends AppCompatActivity {

    private ImageView mPhotoPreview;
    private EditText mPhotoDescription;
    private TextView mPhotoLocation;
    private Button mApproveB;
    private Button mCancelB;
    static final int REQUEST_IMAGE_CAPTURE = 1; //static variable for taking picture using the camera intent
    private LocationDetector myloc;

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

        myloc = new LocationDetector(AddPhoto.this);
        if(!myloc.isGPSEnabled)
            myloc.showSettingsAlert();

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
    /*
    @Override
    public void onBackPressed() {
    }
    */

    @Override
    protected void onStart() {
        super.onStart();
        Double myLat = 0.0;
        Double myLong = 0.0;
        if (myloc.canGetLocation) {
            myLat = myloc.getLatitude();
            myLong = myloc.getLongitude();

            Log.v("get location values", Double.toString(myLat) + "  " + Double.toString(myLong));
            mPhotoLocation.setText("Lat: " + myLat.toString() + " Long: " + myLong.toString());
        }
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState,persistentState);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mPhotoPreview.setImageBitmap(imageBitmap);
        }
    }
}

