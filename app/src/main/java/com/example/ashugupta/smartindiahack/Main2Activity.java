package com.example.ashugupta.smartindiahack;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class Main2Activity extends AppCompatActivity {
    private Camera mCamera = null;
    private static final String TAG = "MainActivity";
    private CameraView mCameraView = null;
    FirebaseStorage firebaseStorage;
    StorageReference storageRef;
    DatabaseReference myRef;
    FirebaseDatabase database;
    String isClicked;
    String url;
    int leftValue, rightValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference().child("Image_Left");
        database = FirebaseDatabase.getInstance();
        //myRef = database.getReference("hasClicked");
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if (mCamera != null) {
            mCameraView = new CameraView(this, mCamera);
            FrameLayout camera_view = (FrameLayout) findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);
        }

       /* myRef = database.getReference("hasClicked");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isClicked = dataSnapshot.getValue(String.class);
                if(TextUtils.equals(isClicked,"Clicked"));
                {
                    mCamera.takePicture(null, null, mPicture);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                storageRef = firebaseStorage.getReference().child("Images_Left");
                mCamera.takePicture(null, null, mPicture);
                //Toast.makeText(MainActivity.this, "Image Clicked", Toast.LENGTH_SHORT).show();
            }
        };
        Timer t = new Timer();
        t.schedule(task, 2000, 4000);

        // final int[] count = {1};
        ImageButton imgClose = (ImageButton) findViewById(R.id.imgClose);

       /* myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String leftValueS = dataSnapshot.getValue(String.class);
                leftValue = Integer.parseInt(leftValueS);
                Log.d(TAG, "Value of Left is: " + leftValue);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        myRef = database.getReference("RightStartTime");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String rightValueS = dataSnapshot.getValue(String.class);
                rightValue = Integer.parseInt(rightValueS);
                Log.d(TAG, "Value of Right is: " + rightValue);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        int time;
        if(rightValue>leftValue)
        {
            time = rightValue + 2;
        }
        else
        {
            time = leftValue+2;
        }
        Log.i(TAG, "onCreate: Time:"+time+" LeftValue:"+ leftValue+ " Right Value:" +rightValue);
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        if(seconds==time)
        {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    storageRef = firebaseStorage.getReference().child("Images_Left" + count[0]);
                    mCamera.takePicture(null, null, mPicture);
                    //Toast.makeText(MainActivity.this, "Image Clicked", Toast.LENGTH_SHORT).show();
                    count[0]++;
                }
            };
            Timer t = new Timer();
            t.schedule(task, 2000, 1000);
        }
        */
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });

    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.i(TAG, "onPictureTaken: Image Clicked");
            myRef = database.getReference("hasClicked");
            myRef.setValue("Clicked");
            UploadTask uploadTask = storageRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override

                public void onFailure(@NonNull Exception exception) {
                    Log.i(TAG, "onFailure: Upload Failed");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i(TAG, "onSuccess: Image Uploaded");
                    String url = taskSnapshot.getDownloadUrl().toString();
                    Log.i(TAG, "onSuccess: URL:" + url);
                    myRef = database.getReference("UrlLeft");
                    myRef.setValue(url);
                }
            });
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
    };


    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }
}


