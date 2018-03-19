package com.example.ganesh.ghour;

import android.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ganesh.ghour.geofencing.UpdateAllGeofencesService;
import com.example.ganesh.ghour.jobscheduler.JobUtil;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.ganesh.ghour.R.id.linerlayout;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private static final String FILE_PROVIDER_AUTHORITY = "com.example.ganesh.ghour.fileprovider";
    private static final String TAG = CameraActivity.class.getSimpleName();
    private static final int MY_PERMISSION_FINE_LOCATION = 123;
    private static final int REQUEST_PLACE_PICKER = 190;
    private EditText mIncidentDescriptionEditText;
    private Button mPostThisIncidentDetailsButton;
    private LinearLayout linearLayout;
    private TextView mIncidentNameTextView;
    private ImageView mIncidentImageView;
    //    Firebase Database
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private String currentuser;
    private StorageReference mStorageRefernce;
    private ProgressDialog mAuthProgessDialog;
    private String mTempPhotoPath;
    private Bitmap mResultsBitmap;
    private String placeIDForDisaster = null;
    private Uri photoURI = null;
    private DatabaseReference mDatabasePlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        initializeScreen();
        mPostThisIncidentDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postThisPicture();
            }
        });

    }


    private void initializeScreen() {

        mIncidentDescriptionEditText = (EditText) findViewById(R.id.camera_edit_text);
        mPostThisIncidentDetailsButton = (Button) findViewById(R.id.camera_post_picture_button);
        linearLayout = (LinearLayout) findViewById(linerlayout);
        mIncidentNameTextView = (TextView) findViewById(R.id.camera_incident_name_textview);
        mIncidentImageView = (ImageView) findViewById(R.id.camera_picture_image_view);

        mAuthProgessDialog = new ProgressDialog(this);
        mAuthProgessDialog.setTitle("Uploading");
        mAuthProgessDialog.setMessage("Your Incident details are uploading");
        mAuthProgessDialog.setCancelable(false);

//        firebase
        mDatabase = FirebaseDatabase.getInstance().getReference().child("incident");
        mDatabasePlaces = FirebaseDatabase.getInstance().getReference().child("placeids");
        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser().getUid();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(currentuser);
        mStorageRefernce = FirebaseStorage.getInstance().getReference();
    }

    private void postThisPicture() {

        String incidentdescription = mIncidentDescriptionEditText.getText().toString();

        if (!TextUtils.isEmpty(incidentdescription)) {
            mAuthProgessDialog.show();
            StorageReference filepath = mStorageRefernce.child("story_image").child(photoURI.getLastPathSegment());

            filepath.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUri = taskSnapshot.getDownloadUrl();

                    final DatabaseReference newIncidentRef = mDatabase.push();

//                  uploading picture to firebase with user details
                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newIncidentRef.child("uid").setValue(currentuser);
                            mDatabasePlaces.child("placeId").push().setValue(placeIDForDisaster);
                            newIncidentRef.child("image").setValue(downloadUri.toString());
                            new ClassifyImage().execute(downloadUri);
                            newIncidentRef.child("details").setValue(mIncidentDescriptionEditText.getText().toString());
                            newIncidentRef.child("name").setValue(dataSnapshot.child("name").getValue()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mAuthProgessDialog.dismiss();
                                    Snackbar.make(linearLayout, "Incident picture uploaded successfully", Snackbar.LENGTH_LONG).show();
                                    mIncidentDescriptionEditText.setText("");
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mAuthProgessDialog.dismiss();
                            Toast.makeText(CameraActivity.this, "Uploading failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("onFailure", "Failed to upload image");
                }
            });

            //Scheduling the job
            JobUtil.scheduleJob(this);
            //onBackPressed();
        }
    }

    public void openCamera(View view) {
        Log.d("CameraActivity1", "OpenCamera");
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            launchCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        Log.d("CameraActivity1", "onRequestPermissionsResult");
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchCamera();
                } else {
                    Toast.makeText(this, "Permission Denied for Camera", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case MY_PERMISSION_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchPlacePicker();
                } else {
                    Toast.makeText(this, "Permission Denied for Location", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    /**
     * Creates a temporary image file and captures a picture to store in it.
     */
    private void launchCamera() {

        Log.d("CameraActivity1", "launchCamera");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(this);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                mTempPhotoPath = photoFile.getAbsolutePath();
                photoURI = FileProvider.getUriForFile(this,
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);
                Log.d("photoURI", photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("CameraActivity1", "onActivityResult");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            processAndSetImage();
        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        } else {
            if (requestCode == REQUEST_PLACE_PICKER) {
                if (resultCode == RESULT_OK && data != null) {
                    Place place = PlacePicker.getPlace(this, data);
                    String placeId = place.getId();
                    Log.d("PlaceID", placeId);
                    placeIDForDisaster = placeId;
                }
            } else {
                Toast.makeText(this, "NO PLACE", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void processAndSetImage() {
        Log.d("CameraActivity1", "processAndSetImage");
        mResultsBitmap = BitmapUtils.resamplePic(this, mTempPhotoPath);
        String savedImagePath = BitmapUtils.saveImage(this, mResultsBitmap);
        Log.d("savedImagePath", savedImagePath);
        mIncidentImageView.setImageBitmap(mResultsBitmap);
    }

    public void openPlacePicker(View view) {
        //request permission for location, if not granted.
        //else launch place picker intent aand on activity result use the place id and store it in field.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
        } else {
            launchPlacePicker();
        }
    }

    private void launchPlacePicker() {

        try {
            PlacePicker.IntentBuilder placeBuilder = new PlacePicker.IntentBuilder();
            Intent placeIntent = placeBuilder.build(this);
            startActivityForResult(placeIntent, REQUEST_PLACE_PICKER);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private class ClassifyImage extends AsyncTask<Uri, Void, Void> {

        @Override
        protected Void doInBackground(Uri... params) {

            OkHttpClient client = new OkHttpClient();

            String url = "http://192.168.43.226:5000/classify_image/" + params[0];
            Log.d("ClassifyImage", url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                Toast.makeText(getApplicationContext(), response.body().string(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
