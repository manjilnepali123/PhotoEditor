package com.example.manjil.photoeditor;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.File;
import java.util.List;

import ly.img.android.ui.activities.CameraPreviewActivity;
import ly.img.android.ui.activities.CameraPreviewIntent;
import ly.img.android.ui.activities.PhotoEditorIntent;


public class MainActivity extends AppCompatActivity {

    public static int CAMERA_PREVIEW_RESULT = 1;
    public static final int PERMISSION_CODE = 2;
    private ImageButton cameraBtn,liveCameraBtn,editorBtn;
    private  String path;
    private File dir;
    private   AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        cameraBtn = (ImageButton)findViewById(R.id.camera);
        //liveCameraBtn = (ImageButton)findViewById(R.id.livecamera);
        editorBtn = (ImageButton)findViewById(R.id.editor);
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + getString(R.string.directory_name);
        dir = new File(path);
        CheckorRequestPermissions();
        cameraBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton view = (ImageButton ) v;
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:

                        // Your action here on button click
                        new CameraPreviewIntent(MainActivity.this)
                                .setExportDir(dir.getPath())
                                .setExportPrefix(getString(R.string.photo_preview_prefix))
                                .setEditorIntent(
                                        new PhotoEditorIntent(MainActivity.this)
                                                .setExportDir(dir.getPath())
                                                .setExportPrefix(getString(R.string.photo_result_prefix))
                                                .destroySourceAfterSave(true)
                                )
                                .startActivityForResult(CAMERA_PREVIEW_RESULT);

                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton view = (ImageButton) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });
        editorBtn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton view = (ImageButton ) v;
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:

                        new ImagePicker.Builder(MainActivity.this)
                                .mode(ImagePicker.Mode.GALLERY)
                                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                                .directory(ImagePicker.Directory.DEFAULT)
                                .extension(ImagePicker.Extension.PNG)
                                .scale(600, 600)
                                .allowMultipleImages(false)
                                .enableDebuggingMode(true)
                                .build();

                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton view = (ImageButton) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_PREVIEW_RESULT) {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String path = data.getStringExtra(CameraPreviewActivity.RESULT_IMAGE_PATH);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
            Toast.makeText(this, getString(R.string.photo_saved_toast_string), Toast.LENGTH_LONG).show();

        }
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            List<String> mPaths = (List<String>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH);
            new PhotoEditorIntent(MainActivity.this)
                    .setSourceImagePath(mPaths.get(0))
                    .setExportDir(dir.getPath())
                    .setExportPrefix(getString(R.string.photo_result_prefix))
                    .destroySourceAfterSave(true)
                    .startActivityForResult(CAMERA_PREVIEW_RESULT);
        }
    }


    @Override
    public void onBackPressed() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
//        builder.setMessage("Do you want to Exit?");
//
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //if user pressed "yes", then he is allowed to exit from application
//                finish();
//            }
//        });
//        builder.setNeutralButton("Rate Us?", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                try {
//                    Uri marketUri = Uri.parse("market://details?id="+getPackageName());
//                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
//                    startActivity(marketIntent);
//                }catch(ActivityNotFoundException e) {
//                    Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName());
//                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
//                    startActivity(marketIntent);
//                }
//            }
//        });
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //if user pressed "yes", then he is allowed to exit from application
//                dialog.cancel();
//
//            }
//        });
//      /*  builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //if user select "No", just cancel this dialog and continue with app
//                dialog.cancel();
//            }
//        });*/
//        AlertDialog alert = builder.create();
//        alert.show();
//    }


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Quit Photo Editor..!!!");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.question);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Are you really want to exit?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public  boolean CheckorRequestPermissions() {
        if (Build.VERSION.SDK_INT >= 28) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
                return false;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                } else {
                    alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setMessage(getString(R.string.permissions_message));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                            CheckorRequestPermissions();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                            Toast.makeText(MainActivity.this,getString(R.string.toast_message),Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    alertDialog.show();
                }
                return;
            }
        }
    }

}